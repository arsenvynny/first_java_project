import java.util.List;

public class Order
{
    private OrderStatus status;
    private Car car;
    private List<Work> work;
    private Mechanic mechanic;

    public Order(Car car)
    {
        this.car = car;

        status = OrderStatus.CREATED;
    }

    public List<Work> getWork() {
        return work;
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
}
