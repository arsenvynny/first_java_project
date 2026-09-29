import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order
{
    private OrderStatus status;
    private Car car;
    private List<Work> works = new ArrayList<>();
    private Mechanic mechanic;

    public Order(Car car)
    {
        this.car = car;

        status = OrderStatus.CREATED;
    }

    public List<Work> getWork() {
        return works;
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

    public void addWork(final Work work)
    {
        if (status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED)
            throw new IllegalStateException("Роботу не можна додати до виконаного або скасованого замовлення");

        this.works.add(work);
    }

    public void assignMechanic(final Mechanic mechanic)
    {
        if (status == OrderStatus.CANCELLED || status == OrderStatus.COMPLETED)
          throw new IllegalStateException("Механіка не можна назначати на скасоване або закінчене замовлення");

        if (mechanic == null)
            throw new IllegalStateException("Немає механіка");

        if (!mechanic.isAvailable())
            throw new IllegalStateException("Механік зайнятий");

        this.mechanic = mechanic;
        mechanic.makeBusy();
    }

    public void diagnose()
    {
        if (status != OrderStatus.CREATED)
            throw new IllegalStateException("Провести діагностику можна тільки для новостворених замовлень");

        status = OrderStatus.DIAGNOSED;
    }

    public void approve()
    {
        if (status != OrderStatus.DIAGNOSED)
            throw new IllegalStateException("Погодити можна тільки після проведення діагностики");

        if (works == null)
            throw new IllegalStateException("Відсутній список робіт");

        status = OrderStatus.APPROVED;
    }

    public void makeInProgress()
    {
        if (status != OrderStatus.APPROVED)
            throw new IllegalStateException("До виконання приступають тільки погоджені замовлення");

        if (mechanic == null)
            throw new IllegalStateException("Механік відсутній");

        status = OrderStatus.IN_PROGRESS;
    }

    public void complete()
    {
        if (status != OrderStatus.IN_PROGRESS)
            throw new IllegalStateException("Закінчити можна тільки ті замовлення, які почали виконуватись");

        mechanic.makeFree();
        status = OrderStatus.COMPLETED;
    }

    public void cancel()
    {
        if (status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED)
            throw new IllegalStateException("Це замовлення не може бути скасованим");

        status = OrderStatus.CANCELLED;

        if (mechanic != null)
            mechanic.makeFree();
    }

    public BigDecimal calculateWorkPrice()
    {
        BigDecimal totalPrice = new BigDecimal("0");

        for (Work item : works)
        {
            totalPrice = totalPrice.add(item.getWorkCost());
        }

        return totalPrice;
    }

    public BigDecimal calculatePartsPrice()
    {
        BigDecimal totalPrice = new BigDecimal("0");

        for (Work item : works)
        {
            totalPrice = totalPrice.add(item.getAutoPartsCost());
        }

        return totalPrice;
    }
}
