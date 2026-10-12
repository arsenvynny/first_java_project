package domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private OrderStatus status;
    private Car car;
    private List<Work> works = new ArrayList<>();
    private Mechanic mechanic;
    private Priority priority;
    private List<Event> events = new ArrayList<>();
    private String ID;

    public Order(Car car, Priority priority) {
        this.car = car;
        this.priority = priority;

        status = OrderStatus.CREATED;

        Event event = new Event("Замовлення створено", EventType.CREATED_ORDER);

        events.add(event);
    }

    public void setID(String ID) {this.ID = ID;}

    public String getID() {return ID;}

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

    public void addWork(final Work work) {
        if (status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED)
            throw new IllegalStateException("Роботу не можна додати до виконаного або скасованого замовлення");

        if (work == null) {
            throw new IllegalStateException("Не вказана робота");
        }

        this.works.add(work);
        Event event = new Event("Додали роботу до замовлення", EventType.WORK_ADDED);

        events.add(event);
    }

    public void assignMechanic(final Mechanic mechanic) {
        if (status == OrderStatus.CANCELLED || status == OrderStatus.COMPLETED)
            throw new IllegalStateException("Механіка не можна назначати на скасоване або закінчене замовлення");

        if (mechanic == null)
            throw new IllegalStateException("Немає механіка");

        if (!mechanic.isAvailable())
            throw new IllegalStateException("Механік зайнятий");

        for (Specialization specializationMechanic : mechanic.getSpecializationList()){
            for (Work work : works){
                if (specializationMechanic == work.getSpecialization()){

                    this.mechanic = mechanic;
                    mechanic.makeBusy();

                    Event event = new Event("Назначено механіка", EventType.MECHANIC_ASSIGNED);

                    events.add(event);
                }
            }
        }

        if(this.mechanic == null){
            throw new IllegalStateException("У механіка немає відповідної спеціальності");
        }
    }

    public void diagnose() {
        if (status != OrderStatus.CREATED)
            throw new IllegalStateException("Провести діагностику можна тільки для новостворених замовлень");

        status = OrderStatus.DIAGNOSED;

        Event event = new Event("Проведено діагностику", EventType.DIAGNOSED_COMPLETED);

        events.add(event);
    }

    public void approve() {
        if (status != OrderStatus.DIAGNOSED)
            throw new IllegalStateException("Погодити можна тільки після проведення діагностики");

        if (works.isEmpty())
            throw new IllegalStateException("Відсутній список робіт");

        status = OrderStatus.APPROVED;

        Event event = new Event("Замовлення підтверджено", EventType.APPROVED_ORDER);

        events.add(event);
    }

    public void makeInProgress() {
        if (status != OrderStatus.APPROVED)
            throw new IllegalStateException("До виконання приступають тільки погоджені замовлення");

        if (mechanic == null)
            throw new IllegalStateException("Механік відсутній");

        status = OrderStatus.IN_PROGRESS;

        Event event = new Event("Розпочалися роботи", EventType.START_WORKS);

        events.add(event);
    }

    public void complete() {
        if (status != OrderStatus.IN_PROGRESS)
            throw new IllegalStateException("Закінчити можна тільки ті замовлення, які почали виконуватись");

        for (Work item : works) {
            if (!item.isCompleted())
                throw new IllegalStateException("Ще не вся робота виконана");
        }

        mechanic.makeFree();
        status = OrderStatus.COMPLETED;

        Event event = new Event("Замовлення виконано", EventType.COMPLETED_ALL_WORKS);

        events.add(event);
    }

    public void cancel() {
        if (status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED)
            throw new IllegalStateException("Це замовлення не може бути скасованим");

        status = OrderStatus.CANCELLED;

        if (mechanic != null)
            mechanic.makeFree();

        Event event = new Event("Замовлення скасовано", EventType.CANCELLED_ORDER);

        events.add(event);
    }

    public Priority getPriority() {
        return priority;
    }

    public BigDecimal calculateWorkPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Work item : works) {
            totalPrice = totalPrice.add(item.getWorkCost());
        }

        if (priority == Priority.URGED)
            totalPrice = totalPrice.add(totalPrice.multiply(Constants.Urgent.FEE));

        return totalPrice;
    }

    public BigDecimal calculatePartsPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Work item : works) {
            totalPrice = totalPrice.add(item.getAutoPartsCost());
        }

        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return  Objects.equals(getCar(), order.getCar()) &&
                Objects.equals(works, order.works) &&
                Objects.equals(getMechanic(), order.getMechanic()) &&
                getPriority() == order.getPriority() &&
                Objects.equals(getEvents(), order.getEvents()) &&
                Objects.equals(getID(), order.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getCar(), works, getMechanic(), getPriority(), getEvents(), getID());
    }
}
