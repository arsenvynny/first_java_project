package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServiceStorage {

    private static ServiceStorage instance;

    private List<Client> clientList = new ArrayList<>();
    private List<Car> carList = new ArrayList<>();
    private List<Mechanic> mechanicList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();

    private ServiceStorage() {
    }

    public static ServiceStorage getInstance() {

        if (instance == null) {
            instance = new ServiceStorage();
        }
        return instance;
    }

    public List<Car> getCarList() {
        return new ArrayList<>(carList);
    }

    public List<Client> getClientList() {
        return new ArrayList<>(clientList);
    }

    public List<Mechanic> getMechanicList() {
        return new ArrayList<>(mechanicList);
    }

    public List<Order> getOrderList() {
        return new ArrayList<>(orderList);
    }

    public void registerCar(final Car car) {
        if (!carList.isEmpty()) {
            for (Car value : carList) {
                if (value.equals(car))
                    throw new IllegalStateException("Це авто уже зареєстроване у сервісі");
            }
        }

        carList.add(car);
    }


    public void registerOrder(final Order order) {
        if (!orderList.isEmpty()) {
            for (Order item : orderList) {
                if (item.equals(order))
                    throw new IllegalStateException("Це замовлення вже записане у сховище");
            }
        }
        orderList.add(order);
    }

    public void registerClient(final Client client) {
        if (!clientList.isEmpty()) {
            for (Client item : clientList) {
                if (item.equals(client))
                    throw new IllegalStateException("Цей клієнт вже записаний у сховище");
            }
        }

        clientList.add(client);
    }

    public void registerMechanic(final Mechanic mechanic) {
        if (!mechanicList.isEmpty()) {
            for (Mechanic item : mechanicList) {
                if (item.equals(mechanic))
                    throw new IllegalStateException("Цей механік вже записаний у сховище");
            }
        }
        mechanicList.add(mechanic);
    }

    public List<Order> searchByCar(final Car car) {
        List<Order> orders = new ArrayList<>();

        for (Order item : orderList) {
            if (item.getCar().equals(car)) {
                orders.add(item);
            }
        }

        return orders;
    }

    public List<Order> searchByStatus(final OrderStatus status) {
        List<Order> orders = new ArrayList<>();

        for (Order item : orderList) {
            if (item.getStatus().equals(status)) {
                orders.add(item);
            }
        }

        return orders;
    }

    public List<Order> searchByMechanic(final Mechanic mechanic) {
        List<Order> orders = new ArrayList<>();

        for (Order item : orderList) {
            if (Objects.equals(item.getMechanic(), mechanic)) {
                orders.add(item);
            }
        }

        return orders;
    }

}
