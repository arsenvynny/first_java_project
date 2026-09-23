import java.util.List;

public class Order
{
    private OrderStatus status;
    private Car car;
    private List<Work> work;
    private Mechanic mechanic;

    public Order(Car car, List<Work> work, Mechanic mechanic)
    {
        this.car = car;
        this.work = work;
        this.mechanic = mechanic;

        status = OrderStatus.CREATED;
    }
}
