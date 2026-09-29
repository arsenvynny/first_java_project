import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServiceStorage {

    private List<Client> clientList = new ArrayList<>();
    private List<Car> carList = new ArrayList<>();
    private List<Mechanic> mechanicList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();

    ServiceStorage()
    {}

    public List<Car> getCarList() {
        return carList;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public List<Mechanic> getMechanicList() {
        return mechanicList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void registerCar(final Car car)
    {
        for(Car value : carList)
            if (value.getVinCode().equals(car.getVinCode()))
                throw new IllegalStateException("Це авто уже зареєстроване у сервісі");

        carList.add(car);
    }
    public void registerOrder(final Order order)
    {
        for (Order item : orderList)
        {
            if (item.equals(order))
                throw new IllegalStateException("Це замовлення вже записане у сховище");
        }
        orderList.add(order);
    }

    public void registerClient(final Client client)
    {
        for (Client item : clientList)
        {
            if (item.equals(client))
                throw new IllegalStateException("Цей клієнт вже записаний у сховище");
        }

        clientList.add(client);
    }

    public void registerMechanic(final Mechanic mechanic)
    {
        for (Mechanic item : mechanicList)
        {
            if (item.equals(mechanic))
                throw new IllegalStateException("Цей механік вже записаний у сховище");
        }
        mechanicList.add(mechanic);
    }

    public List<Order> searchByCar (final Car car)
    {
        List<Order> orders = new ArrayList<>();

        for (Order item : orderList)
        {
            if (item.getCar().equals(car))
            {
                orders.add(item);
            }
        }

        return orders;
    }

    public List<Order> searchByStatus (final OrderStatus status)
    {
        List<Order> orders = new ArrayList<>();

        for (Order item : orderList)
        {
            if (item.getStatus().equals(status))
            {
                orders.add(item);
            }
        }

        return orders;
    }

    public List<Order> searchByMechanic (final Mechanic mechanic)
    {
        List<Order> orders = new ArrayList<>();

        for (Order item : orderList)
        {
            if (item.getMechanic().equals(mechanic))
            {
                orders.add(item);
            }
        }

        return orders;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServiceStorage that = (ServiceStorage) o;
        return  Objects.equals(clientList, that.clientList) &&
                Objects.equals(carList, that.carList) &&
                Objects.equals(mechanicList, that.mechanicList) &&
                Objects.equals(orderList, that.orderList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientList, carList, mechanicList, orderList);
    }
}
