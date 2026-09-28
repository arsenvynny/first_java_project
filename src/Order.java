import java.util.ArrayList;
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
        this.work = new ArrayList<>();
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

    public void addWork(Work work)
    {
        this.work.add(work);   
    }

    public void assignMechanic(Mechanic mechanic)
    {
        if (status == OrderStatus.CANCELLED || status == OrderStatus.COMPLETED)
          throw new IllegalArgumentException("Механіка не можна назначати на скачоване або закінчене замовлення");

        if (mechanic == null)
            throw new IllegalArgumentException("Немає механіка");

        if (mechanic.getStatus())
            throw new IllegalArgumentException("Механік зайнятий");

        this.mechanic = mechanic;
        mechanic.makeBusy();
    }

    public void diagnose()
    {
        if (status != OrderStatus.CREATED)
            throw new RuntimeException("Провести діагностику можна тільки для новостворених замовлень");

        status = OrderStatus.DIAGNOSED;
    }

    public void approve()
    {
        if (status != OrderStatus.DIAGNOSED)
            throw new RuntimeException("Погодити можна тільки після проведення діагностики");

        status = OrderStatus.APPROVED;
    }

    public void make_in_progress()
    {
        if (status != OrderStatus.APPROVED)
            throw new RuntimeException("До виконання приступають тільки погоджені замовлення");

        status = OrderStatus.IN_PROGRESS;
    }

    public void complete()
    {
        if (status != OrderStatus.IN_PROGRESS)
            throw new RuntimeException("Закінчити можна тільки ті замовлення, які почали виконуватись");

        mechanic.makeFree();
        status = OrderStatus.COMPLETED;
    }

    public void cancel()
    {
        if (status == OrderStatus.COMPLETED)
            throw new RuntimeException("Це замовлення вже виконане");

        mechanic.makeFree();

        status = OrderStatus.CANCELLED;
        mechanic.makeFree();
    }
}
