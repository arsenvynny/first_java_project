package service;

import domain.*;
import pricing.OrderPriceInfo;
import pricing.PricePolicy;
import repository.RepairOrderRepository;

import java.math.BigDecimal;
import java.util.Objects;

public class RepairOrderService {
    private final RepairOrderRepository repository;
    private final PricePolicy pricePolicy;

    public RepairOrderService(RepairOrderRepository repository, PricePolicy pricePolicy) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.pricePolicy = Objects.requireNonNull(pricePolicy, "pricePolicy must not be null");
    }

    public Order createOrder(Car car, Priority priority) {
        if (car == null)
            throw new IllegalStateException("Car must be specified");
        if (priority == null)
            throw new IllegalStateException("Priority must be specified");
        Order order = new Order(car, priority);
        addEvent(order, "Repair order created", EventType.CREATED_ORDER);
        return repository.save(order);
    }


    public void addWork(Order order, Work work) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() != OrderStatus.DIAGNOSED)
            throw new IllegalStateException("Work cannot be added to this order in its current status");

        if (work == null)
            throw new IllegalStateException("Work must be specified");

        order.getWorks().add(work);
        addEvent(order, "Work added to repair order", EventType.WORK_ADDED);
        repository.save(order);
    }

    public void assignMechanic(Order order, Mechanic mechanic) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() != OrderStatus.APPROVED)
            throw new IllegalStateException("A mechanic can only be assigned to an approved order");

        if (mechanic == null)
            throw new IllegalStateException("A mechanic must be specified");

        if (order.getMechanic() != null)
            throw new IllegalStateException("A mechanic has already been assigned");

        if (!mechanic.isAvailable())
            throw new IllegalStateException("The mechanic is busy");

        for (Work work : order.getWorks()) {
            if (!mechanic.canPerform(work))
                throw new IllegalStateException("The mechanic does not have the required specialization");
        }

        order.setMechanic(mechanic);
        mechanic.makeBusy();
        addEvent(order, "Mechanic assigned", EventType.MECHANIC_ASSIGNED);
        repository.save(order);
    }

    public void diagnose(Order order) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() != OrderStatus.CREATED)
            throw new IllegalStateException("Diagnosis can only be performed on newly created orders");

        order.setStatus(OrderStatus.DIAGNOSED);
        addEvent(order, "Diagnosis completed", EventType.DIAGNOSED_COMPLETED);
        repository.save(order);
    }

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

    public void complete(Order order) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() != OrderStatus.IN_PROGRESS)
            throw new IllegalStateException("Only in-progress orders can be completed");

        for (Work item : order.getWorks()) {
            if (!item.isCompleted())
                throw new IllegalStateException("Not all work items have been completed");
        }

        order.getMechanic().makeFree();
        order.setStatus(OrderStatus.COMPLETED);
        addEvent(order, "Repair order completed", EventType.COMPLETED_ALL_WORKS);
        repository.save(order);
    }

    public void cancel(Order order) {
        order = getExistingOrder(order.getId());

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED)
            throw new IllegalStateException("This order cannot be cancelled");

        order.setStatus(OrderStatus.CANCELLED);
        if (order.getMechanic() != null)
            order.getMechanic().makeFree();

        addEvent(order, "Repair order cancelled", EventType.CANCELLED_ORDER);
        repository.save(order);
    }

    public BigDecimal getWorkPrice(Order order) {
        BigDecimal totalWorkPrice = BigDecimal.ZERO;
        for (Work work : order.getWorks())
            totalWorkPrice = totalWorkPrice.add(work.getWorkCost());
        return totalWorkPrice;
    }

    public BigDecimal getPartsPrice(Order order) {
        BigDecimal totalPartsPrice = BigDecimal.ZERO;
        for (Work work : order.getWorks())
            totalPartsPrice = totalPartsPrice.add(work.getAutoPartsCost());
        return totalPartsPrice;
    }

    public BigDecimal getTotalOrderPrice(Order order) {
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
}
