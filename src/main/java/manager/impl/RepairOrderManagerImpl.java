package manager.impl;

import domain.*;
import lombok.RequiredArgsConstructor;
import manager.RepairOrderManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pricing.OrderPriceInfo;
import pricing.PricePolicy;
import service.CarService;
import service.EventService;
import service.MechanicService;
import service.OrderService;
import service.WorkService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class RepairOrderManagerImpl implements RepairOrderManager {
    private final PricePolicy pricePolicy;
    private final CarService carService;
    private final EventService eventService;
    private final MechanicService mechanicService;
    private final OrderService orderService;
    private final WorkService workService;

    @Override
    public Order createOrder(Long carId, Priority priority) {
        if (carId == null) {
            throw new IllegalArgumentException("A persisted car must be specified");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Order priority must be specified");
        }

        Order order = new Order();
        order.setCar(carService.findById(carId));
        order.setPriority(priority);
        Order createdOrder = orderService.create(order);
        addEvent(createdOrder, "Repair order created", EventType.CREATED_ORDER);
        return createdOrder;
    }

    @Override
    public void addWorks(Long orderId, List<Work> works) {
        Order order = getExistingOrder(orderId);
        if (order.getStatus() != OrderStatus.DIAGNOSED) {
            throw new IllegalStateException("Work cannot be added to this order in its current status");
        }
        if (works == null || works.isEmpty()) {
            throw new IllegalArgumentException("At least one work item must be specified");
        }
        if (works.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("A persisted work item must be specified");
        }

        works.stream()
                .map(workService::create)
                .forEach(saved -> {
                    saved.setOrder(order);
                    order.getWorks().add(saved);
                    addEvent(order, "Work " + saved.getId() + " added to repair order", EventType.WORK_ADDED);
                });
    }

    @Override
    public void diagnose(Long orderId) {
        Order order = getExistingOrder(orderId);
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException("Diagnosis can only be performed on newly created orders");
        }
        order.setStatus(OrderStatus.DIAGNOSED);
        addEvent(order, "Diagnosis completed", EventType.DIAGNOSED_COMPLETED);
    }

    @Override
    public void approve(Long orderId) {
        Order order = getExistingOrder(orderId);
        if (order.getStatus() != OrderStatus.DIAGNOSED) {
            throw new IllegalStateException("Only diagnosed orders can be approved");
        }
        if (order.getWorks().isEmpty()) {
            throw new IllegalStateException("At least one work item is required for approval");
        }
        order.setStatus(OrderStatus.APPROVED);
        addEvent(order, "Repair order approved", EventType.APPROVED_ORDER);
    }

    @Override
    public void assignMechanic(Long orderId, Long mechanicId) {
        Order order = getExistingOrder(orderId);
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new IllegalStateException("A mechanic can only be assigned to an approved order");
        }
        if (mechanicId == null) {
            throw new IllegalArgumentException("A mechanic must be specified");
        }
        if (order.getMechanic() != null) {
            throw new IllegalStateException("A mechanic has already been assigned");
        }

        Mechanic mechanic = mechanicService.findById(mechanicId);
        if (!mechanic.isAvailable()) {
            throw new IllegalStateException("The mechanic is busy");
        }
        order.getWorks().stream()
                .filter(work -> !mechanic.getSpecializationList().contains(work.getSpecialization()))
                .findFirst()
                .ifPresent(work -> {
                    throw new IllegalStateException("The mechanic does not have the required specialization");
                });

        order.setMechanic(mechanic);
        mechanic.setAvailable(false);
        addEvent(order, "Mechanic assigned", EventType.MECHANIC_ASSIGNED);
    }

    @Override
    public void makeInProgress(Long orderId) {
        Order order = getExistingOrder(orderId);
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new IllegalStateException("Only approved orders can be started");
        }
        if (order.getMechanic() == null) {
            throw new IllegalStateException("A mechanic must be assigned before work can start");
        }
        order.setStatus(OrderStatus.IN_PROGRESS);
        addEvent(order, "Work started", EventType.START_WORKS);
    }

    @Override
    public void markWorkDone(Long workId) {
        Work work = getExistingWork(workId);
        Order order = work.getOrder();
        if (order == null || order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Work can only be completed while its order is in progress");
        }
        if (work.isCompleted()) {
            throw new IllegalStateException("This work item has already been completed");
        }
        work.setCompleted(true);
    }

    @Override
    public void complete(Long orderId) {
        Order order = getExistingOrder(orderId);
        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only in-progress orders can be completed");
        }
        if (order.getWorks().stream().anyMatch(work -> !work.isCompleted())) {
            throw new IllegalStateException("Not all work items have been completed");
        }

        order.getMechanic().setAvailable(true);
        order.setStatus(OrderStatus.COMPLETED);
        order.setFinalCost(getCommercialOrderPrice(order));
        order.setCompletedAt(LocalDateTime.now());
        addEvent(order, "Repair order completed", EventType.COMPLETED_ALL_WORKS);
    }

    @Override
    public void cancel(Long orderId) {
        Order order = getExistingOrder(orderId);
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("This order cannot be cancelled");
        }

        List<Work> completedWorks = order.getWorks().stream()
                .filter(Work::isCompleted)
                .toList();
        if (!completedWorks.isEmpty()) {
            order.getWorks().stream()
                    .filter(work -> !work.isCompleted())
                    .toList()
                    .forEach(work -> {
                        order.getWorks().remove(work);
                        work.setOrder(null);
                        workService.delete(work.getId());
                    });
            order.setFinalCost(getCommercialOrderPrice(order, completedWorks));
        }

        order.setStatus(OrderStatus.CANCELLED);
        if (order.getMechanic() != null) {
            order.getMechanic().setAvailable(true);
        }
        addEvent(order, "Repair order cancelled", EventType.CANCELLED_ORDER);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalOrderPrice(Long orderId) {
        Order order = getExistingOrder(orderId);
        return getWorkPrice(order).add(getPartsPrice(order));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCommercialOrderPrice(Long orderId) {
        return getCommercialOrderPrice(getExistingOrder(orderId));
    }

    private BigDecimal getWorkPrice(Order order) {
        return order.getWorks().stream()
                .map(Work::getWorkCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getPartsPrice(Order order) {
        return order.getWorks().stream()
                .map(Work::getAutoPartsCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getCommercialOrderPrice(Order order) {
        return getCommercialOrderPrice(order, order.getWorks());
    }

    private BigDecimal getCommercialOrderPrice(Order order, List<Work> works) {
        BigDecimal workPrice = works.stream()
                .map(Work::getWorkCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal partsPrice = works.stream()
                .map(Work::getAutoPartsCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return pricePolicy.getTotalPrice(
                new OrderPriceInfo(workPrice, partsPrice, order.getPriority() == Priority.URGED));
    }

    private void addEvent(Order order, String description, EventType type) {
        Event event = eventService.record(order, type, description);
        order.getEvents().add(event);
    }

    private Order getExistingOrder(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("A persisted repair order must be specified");
        }
        return orderService.findById(orderId);
    }

    private Work getExistingWork(Long workId) {
        if (workId == null) {
            throw new IllegalArgumentException("A persisted work item must be specified");
        }
        return workService.findById(workId);
    }
}
