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

    public void registerCar(Car car)
    {
        for(Car value : carList)
            if (Objects.equals(value.getVinCode(), car.getVinCode()))
                throw new RuntimeException("Це авто уже зареєстроване у сервісі");

        carList.add(car);
    }

    public void registerClient(Client client)
    {
        clientList.add(client);
    }

    public void registerMechanic(Mechanic mechanic)
    {
        mechanicList.add(mechanic);
    }
}
