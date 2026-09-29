import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CarService {

    private List<Client> clientList;
    private List<Car> carList;
    private List<Mechanic> mechanicList;

    CarService()
    {
        clientList = new ArrayList<Client>();
        carList = new ArrayList<Car>();
        mechanicList = new ArrayList<Mechanic>();
    }

    public void registerCar(final Car car)
    {
        for(Car value : carList)
            if (value.equals(car))
                throw new IllegalStateException("Це авто уже зареєстроване у сервісі");

        carList.add(car);
    }

    public void registerClient(final Client client)
    {
        clientList.add(client);
    }

    public void registerMechanic(final Mechanic mechanic)
    {
        mechanicList.add(mechanic);
    }
}
