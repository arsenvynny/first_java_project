package domain.car;

import domain.Car;
import domain.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreatingCarTest {

    private Client client;

    @BeforeEach
    void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
    }

    @Test
    void Car_constructor_setsCarFields() {

        String mark = "Toyota";
        String model = "Camry";
        String vinCode = "BC0000AT";
        int year = 2026;


        Car car = new Car(mark, model, year, client, vinCode);

        assertEquals(mark, car.getMark());
        assertEquals(model, car.getModel());
        assertEquals(year, car.getYear());
        assertEquals(vinCode, car.getVinCode());
        assertEquals(client, car.getOwner());
    }

    @Test
    void Car_constructorWithNullOrEmptyModel_ExceptionThrows() {

        String mark = "Toyota";
        String model = null;
        String vinCode = "BC0000AT";
        int year = 2026;


        assertThrows(IllegalArgumentException.class,
                () -> new Car(mark, model, year, client, vinCode));

        String model2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Car(mark, model2, year, client, vinCode));

    }

    @Test
    void Car_constructorWithNullOrEmptyMark_ExceptionThrows() {

        String mark = null;
        String model = "Camry";
        String vinCode = "BC0000AT";
        int year = 2026;


        assertThrows(IllegalArgumentException.class,
                () -> new Car(mark, model, year, client, vinCode));

        String mark2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Car(mark2, model, year, client, vinCode));

    }

    @Test
    void Car_constructorWithNullOrEmptyVinCode_ExceptionThrows() {

        String mark = "Toyota";
        String model = "Camry";
        String vinCode = null;
        int year = 2026;


        assertThrows(IllegalArgumentException.class,
                () -> new Car(mark, model, year, client, vinCode));

        String vinCode2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Car(mark, model, year, client, vinCode2));
    }
}
