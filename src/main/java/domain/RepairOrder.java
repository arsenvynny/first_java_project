package domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RepairOrder {
    private OrderStatus status = OrderStatus.CREATED;;
    private Car car;
    private List<Work> works = new ArrayList<>();
    private Mechanic mechanic;
    private Priority priority;
    private List<Event> events = new ArrayList<>();
    private Long id;

    public RepairOrder(Car car, Priority priority) {
        this.car = car;
        this.priority = priority;

        Event event = new Event("Repair order created", EventType.CREATED_ORDER);

        events.add(event);
    }

    public void setId(Long Id) {
        this.id = Id;
    }

    public Long getID() {return id;}

    public List<Work> getWork() {
        return new ArrayList<>(works);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public Car getCar() {
        return car;
    }

    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    public Priority getPriority() {
        return priority;
    }

    public void addWork(final Work work) {
        if (status != OrderStatus.DIAGNOSED)
            throw new IllegalStateException("Work cannot be added to this order in its current status");

        if (work == null) {
            throw new IllegalStateException("Work must be specified");
        }

        this.works.add(work);
        Event event = new Event("Work added to repair order", EventType.WORK_ADDED);

        events.add(event);
    }

    public void assignMechanic(final Mechanic mechanic) {
        if (status != OrderStatus.APPROVED)
            throw new IllegalStateException("A mechanic can only be assigned to an approved order");

        if (mechanic == null)
            throw new IllegalStateException("A mechanic must be specified");

        if (this.mechanic != null){
            throw new IllegalStateException("A mechanic has already been assigned");
        }

        if (!mechanic.isAvailable())
            throw new IllegalStateException("The mechanic is busy");

        for (Work work : works){
            if (!mechanic.canPerform(work)){
                throw new IllegalStateException("The mechanic does not have the required specialization");
            }
        }

        this.mechanic = mechanic;
        mechanic.makeBusy();

        Event event = new Event("Mechanic assigned", EventType.MECHANIC_ASSIGNED);

        events.add(event);
    }

    public void diagnose() {
        if (status != OrderStatus.CREATED)
            throw new IllegalStateException("Diagnosis can only be performed on newly created orders");

        status = OrderStatus.DIAGNOSED;

        Event event = new Event("Diagnosis completed", EventType.DIAGNOSED_COMPLETED);

        events.add(event);
    }

    public void approve() {

        if (works.isEmpty())
            throw new IllegalStateException("At least one work item is required for approval");

        status = OrderStatus.APPROVED;

        Event event = new Event("Repair order approved", EventType.APPROVED_ORDER);

        events.add(event);
    }

    public void makeInProgress() {
        if (status != OrderStatus.APPROVED)
            throw new IllegalStateException("Only approved orders can be started");

        if (mechanic == null)
            throw new IllegalStateException("A mechanic must be assigned before work can start");

        status = OrderStatus.IN_PROGRESS;

        Event event = new Event("Work started", EventType.START_WORKS);

        events.add(event);
    }

    public void complete() {
        if (status != OrderStatus.IN_PROGRESS)
            throw new IllegalStateException("Only in-progress orders can be completed");

        for (Work item : works) {
            if (!item.isCompleted())
                throw new IllegalStateException("Not all work items have been completed");
        }

        mechanic.makeFree();
        status = OrderStatus.COMPLETED;

        Event event = new Event("Repair order completed", EventType.COMPLETED_ALL_WORKS);

        events.add(event);
    }

    public void cancel() {
        if (status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED)
            throw new IllegalStateException("This order cannot be cancelled");

        status = OrderStatus.CANCELLED;

        if (mechanic != null)
            mechanic.makeFree();

        Event event = new Event("Repair order cancelled", EventType.CANCELLED_ORDER);

        events.add(event);
    }

    public BigDecimal getWorkPrice(){

        BigDecimal totalWorkPrice = BigDecimal.ZERO;

        for (Work work : works){
            totalWorkPrice = totalWorkPrice.add(work.getWorkCost());
        }

        return totalWorkPrice;
    }

    public BigDecimal getPartsPrice(){

        BigDecimal totalWorkPrice = BigDecimal.ZERO;

        for (Work work : works){
            totalWorkPrice = totalWorkPrice.add(work.getAutoPartsCost());
        }

        return totalWorkPrice;
    }

    public BigDecimal getTotalOrderPrice(){
        return getPartsPrice().add(getWorkPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RepairOrder order = (RepairOrder) o;
        return  Objects.equals(getCar(), order.getCar()) &&
                Objects.equals(getMechanic(), order.getMechanic()) &&
                getPriority() == order.getPriority() &&
                Objects.equals(getID(), order.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCar(), getMechanic(), getPriority(), getID());
    }
}
