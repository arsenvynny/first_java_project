package service.impl;

import domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pricing.OrderPriceInfo;
import pricing.PricePolicy;
import service.MechanicService;
import service.OrderService;
import service.RepairService;
import service.WorkService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class RepairServiceImpl implements RepairService {
    private final PricePolicy pricePolicy;
    private final MechanicService mechanicService;
    private final WorkService workService;
    private final OrderService orderService;

    @Override
    public Order create(Order order) {
        addEvent(order, "Repair order created", EventType.CREATED_ORDER);
        return orderService.create(order);
    }

    @Override
    public void addWorks(Long orderId, List<Long> workIds) {
        Order managedOrder = getExistingOrder(orderId);

        if (managedOrder.getStatus() != OrderStatus.DIAGNOSED)
            throw new IllegalStateException("Work cannot be added to this order in its current status");

        if (workIds == null || workIds.isEmpty())
            throw new IllegalArgumentException("At least one work item must be specified");

        if (workIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("A persisted work item must be specified");
        }

        workIds.forEach(workId -> {
            Work managedWork = workService.findById(workId);
            if (managedWork.getOrder() != null
                    && !managedWork.getOrder().getId().equals(managedOrder.getId())) {
                throw new IllegalStateException("Work is already assigned to another order");
            }
            if (managedOrder.getWorks().stream().noneMatch(existing -> existing.getId().equals(managedWork.getId()))) {
                managedWork.setOrder(managedOrder);
                managedOrder.getWorks().add(managedWork);
            }
            addEvent(managedOrder, "Work + " + managedWork.getId() + " added to repair order", EventType.WORK_ADDED);
            orderService.update(managedOrder);
        });
    }

    @Override
    public void markWorkDone(Long workId) {
        Work managedWork = getExistingWork(workId);
        Order order = managedWork.getOrder();
        if (order == null || order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Work can only be completed while its order is in progress");
        }
        if (managedWork.isCompleted()) {
            throw new IllegalStateException("This work item has already been completed");
        }
        managedWork.setCompleted(true);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isWorkCompleted(Long workId) {
        return getExistingWork(workId).isCompleted();
    }

    @Override
    public void assignMechanic(Long orderId, Long mechanicId) {
        Order order = getExistingOrder(orderId);

        if (order.getStatus() != OrderStatus.APPROVED)
            throw new IllegalStateException("A mechanic can only be assigned to an approved order");

        if (mechanicId == null)
            throw new IllegalArgumentException("A mechanic must be specified");

        if (order.getMechanic() != null)
            throw new IllegalStateException("A mechanic has already been assigned");

        Mechanic managedMechanic = mechanicService.findById(mechanicId);
        if (!managedMechanic.isAvailable())
            throw new IllegalStateException("The mechanic is busy");

        order.getWorks().stream()
                .filter(work -> !managedMechanic.getSpecializationList().contains(work.getSpecialization()))
                .findFirst()
                .ifPresent(work -> {
                    throw new IllegalStateException("The mechanic does not have the required specialization");
                });

        order.setMechanic(managedMechanic);
        managedMechanic.setAvailable(false);
        addEvent(order, "Mechanic assigned", EventType.MECHANIC_ASSIGNED);
        orderService.update(order);
    }

    @Override
    public void diagnose(Long orderId) {
        Order order = getExistingOrder(orderId);

        if (order.getStatus() != OrderStatus.CREATED)
            throw new IllegalStateException("Diagnosis can only be performed on newly created orders");

        order.setStatus(OrderStatus.DIAGNOSED);
        addEvent(order, "Diagnosis completed", EventType.DIAGNOSED_COMPLETED);
        orderService.update(order);
    }

    @Override
    public void approve(Long orderId) {
        Order order = getExistingOrder(orderId);

        if (order.getStatus() != OrderStatus.DIAGNOSED)
            throw new IllegalStateException("Only diagnosed orders can be approved");

        if (order.getWorks().isEmpty())
            throw new IllegalStateException("At least one work item is required for approval");

        order.setStatus(OrderStatus.APPROVED);
        addEvent(order, "Repair order approved", EventType.APPROVED_ORDER);
        orderService.update(order);
    }

    @Override
    public void makeInProgress(Long orderId) {
        Order order = getExistingOrder(orderId);

        if (order.getStatus() != OrderStatus.APPROVED)
            throw new IllegalStateException("Only approved orders can be started");

        if (order.getMechanic() == null)
            throw new IllegalStateException("A mechanic must be assigned before work can start");

        order.setStatus(OrderStatus.IN_PROGRESS);
        addEvent(order, "Work started", EventType.START_WORKS);
        orderService.update(order);
    }

    @Override
    public void complete(Long orderId) {
        Order order = getExistingOrder(orderId);

        if (order.getStatus() != OrderStatus.IN_PROGRESS)
            throw new IllegalStateException("Only in-progress orders can be completed");

        order.getWorks().stream()
                .filter(item -> !item.isCompleted())
                .findFirst()
                .ifPresent(item -> {
                    throw new IllegalStateException("Not all work items have been completed");
                });

        order.getMechanic().setAvailable(true);
        order.setStatus(OrderStatus.COMPLETED);
        order.setFinalCost(getCommercialOrderPrice(order));
        order.setCompletedAt(LocalDateTime.now());
        addEvent(order, "Repair order completed", EventType.COMPLETED_ALL_WORKS);
        orderService.update(order);
    }

    @Override
    public void cancel(Long orderId) {
        Order order = getExistingOrder(orderId);

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED)
            throw new IllegalStateException("This order cannot be cancelled");

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
        if (order.getMechanic() != null)
            order.getMechanic().setAvailable(true);

        addEvent(order, "Repair order cancelled", EventType.CANCELLED_ORDER);
        orderService.update(order);
    }

    @Override
    public BigDecimal getWorkPrice(Order order) {
        return order.getWorks().stream()
                .map(Work::getWorkCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getPartsPrice(Order order) {
        return order.getWorks().stream()
                .map(Work::getAutoPartsCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalOrderPrice(Order order) {
        return getPartsPrice(order).add(getWorkPrice(order));
    }

    public BigDecimal getCommercialOrderPrice(Order order) {
        return getCommercialOrderPrice(order, order.getWorks());
    }

    private BigDecimal getCommercialOrderPrice(Order order, List<Work> works) {
        return pricePolicy.getTotalPrice(new OrderPriceInfo(
                works.stream().map(Work::getWorkCost).reduce(BigDecimal.ZERO, BigDecimal::add),
                works.stream().map(Work::getAutoPartsCost).reduce(BigDecimal.ZERO, BigDecimal::add),
                order.getPriority() == Priority.URGED));
    }

    private void addEvent(Order order, String description, EventType type) {
        Event event = new Event(description, type);
        event.setOrder(order);
        order.getEvents().add(event);
    }

    private Order getExistingOrder(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("A persisted repair order must be specified");
        }
        return orderService.findById(id);
    }

    private Work getExistingWork(Long workId) {
        if (workId == null) {
            throw new IllegalArgumentException("A persisted work item must be specified");
        }
        return workService.findById(workId);
    }
}
