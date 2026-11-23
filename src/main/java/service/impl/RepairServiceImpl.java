package service.impl;

import domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pricing.OrderPriceInfo;
import pricing.PricePolicy;
import repository.RepairOrderRepository;
import service.MechanicService;
import service.RepairService;
import service.WorkService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class RepairServiceImpl implements RepairService {
    private final RepairOrderRepository repository;
    private final PricePolicy pricePolicy;
    private final MechanicService mechanicService;
    private final WorkService workService;

    @Override
    public void addWork(Order order, Work work) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() != OrderStatus.DIAGNOSED)
            throw new IllegalStateException("Work cannot be added to this order in its current status");

        if (work == null || work.getId() == null)
            throw new IllegalStateException("A persisted work item must be specified");

        Work managedWork = workService.findById(work.getId());
        if (managedWork.getOrder() != null
                && !managedWork.getOrder().getId().equals(order.getId())) {
            throw new IllegalStateException("Work is already assigned to another order");
        }
        if (order.getWorks().stream().noneMatch(existing -> existing.getId().equals(managedWork.getId()))) {
            managedWork.setOrder(order);
            order.getWorks().add(managedWork);
        }
        addEvent(order, "Work added to repair order", EventType.WORK_ADDED);
        repository.save(order);
    }

    @Override
    public void markWorkDone(Work work) {
        Work managedWork = getExistingWork(work);
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
    public boolean isWorkCompleted(Work work) {
        return getExistingWork(work).isCompleted();
    }

    @Override
    public void assignMechanic(Order order, Mechanic mechanic) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() != OrderStatus.APPROVED)
            throw new IllegalStateException("A mechanic can only be assigned to an approved order");

        if (mechanic == null)
            throw new IllegalStateException("A mechanic must be specified");

        if (order.getMechanic() != null)
            throw new IllegalStateException("A mechanic has already been assigned");

        if (mechanic.getId() == null)
            throw new IllegalArgumentException("A persisted mechanic must be specified");

        mechanic = mechanicService.findById(mechanic.getId());
        if (!mechanic.isStatus())
            throw new IllegalStateException("The mechanic is busy");

        for (Work work : order.getWorks()) {
            if (!mechanic.getSpecializationList().contains(work.getSpecialization()))
                throw new IllegalStateException("The mechanic does not have the required specialization");
        }

        order.setMechanic(mechanic);
        mechanic.setStatus(false);
        addEvent(order, "Mechanic assigned", EventType.MECHANIC_ASSIGNED);
        repository.save(order);
    }

    @Override
    public void diagnose(Order order) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() != OrderStatus.CREATED)
            throw new IllegalStateException("Diagnosis can only be performed on newly created orders");

        order.setStatus(OrderStatus.DIAGNOSED);
        addEvent(order, "Diagnosis completed", EventType.DIAGNOSED_COMPLETED);
        repository.save(order);
    }

    @Override
    public void approve(Order order) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() != OrderStatus.DIAGNOSED)
            throw new IllegalStateException("Only diagnosed orders can be approved");

        if (order.getWorks().isEmpty())
            throw new IllegalStateException("At least one work item is required for approval");

        order.setStatus(OrderStatus.APPROVED);
        addEvent(order, "Repair order approved", EventType.APPROVED_ORDER);
        repository.save(order);
    }

    @Override
    public void makeInProgress(Order order) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() != OrderStatus.APPROVED)
            throw new IllegalStateException("Only approved orders can be started");

        if (order.getMechanic() == null)
            throw new IllegalStateException("A mechanic must be assigned before work can start");

        order.setStatus(OrderStatus.IN_PROGRESS);
        addEvent(order, "Work started", EventType.START_WORKS);
        repository.save(order);
    }

    @Override
    public void complete(Order order) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() != OrderStatus.IN_PROGRESS)
            throw new IllegalStateException("Only in-progress orders can be completed");

        for (Work item : order.getWorks()) {
            if (!item.isCompleted())
                throw new IllegalStateException("Not all work items have been completed");
        }

        order.getMechanic().setStatus(true);
        order.setStatus(OrderStatus.COMPLETED);
        addEvent(order, "Repair order completed", EventType.COMPLETED_ALL_WORKS);
        repository.save(order);
    }

    @Override
    public void cancel(Order order) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED)
            throw new IllegalStateException("This order cannot be cancelled");

        order.setStatus(OrderStatus.CANCELLED);
        if (order.getMechanic() != null)
            order.getMechanic().setStatus(true);

        addEvent(order, "Repair order cancelled", EventType.CANCELLED_ORDER);
        repository.save(order);
    }

    @Override
    public BigDecimal getWorkPrice(Order order) {
        BigDecimal totalWorkPrice = BigDecimal.ZERO;
        for (Work work : order.getWorks())
            totalWorkPrice = totalWorkPrice.add(work.getWorkCost());
        return totalWorkPrice;
    }

    @Override
    public BigDecimal getPartsPrice(Order order) {
        BigDecimal totalPartsPrice = BigDecimal.ZERO;
        for (Work work : order.getWorks())
            totalPartsPrice = totalPartsPrice.add(work.getAutoPartsCost());
        return totalPartsPrice;
    }

    @Override
    public BigDecimal getTotalOrderPrice(Order order) {
        return getPartsPrice(order).add(getWorkPrice(order));
    }

    public BigDecimal getCommercialOrderPrice(Order order) {
        return pricePolicy.getTotalPrice(new OrderPriceInfo(
                getWorkPrice(order),
                getPartsPrice(order),
                order.getPriority() == Priority.URGED));
    }

    private void addEvent(Order order, String description, EventType type) {
        order.getEvents().add(new Event(description, type));
    }

    private Order getExistingOrder(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repair order with id " + id + " does not exist"));
    }

    private Work getExistingWork(Work work) {
        if (work == null || work.getId() == null) {
            throw new IllegalArgumentException("A persisted work item must be specified");
        }
        return workService.findById(work.getId());
    }
}
