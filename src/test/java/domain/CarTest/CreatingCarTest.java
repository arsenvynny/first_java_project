package domain.CarTest;

import domain.Car;
import domain.Client;
import domain.Mechanic;
import domain.Specialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepairOrderRepository;
import repository.RepairOrderRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreatingCarTest {

    private Client client;

    @BeforeEach
    void setUp(){

        client = new Client("Arsen", "Vynnytskyi");
    }

    @Test
    void createCar_creatingCar_isCreated(){

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
    void createCar_creatingCarWithNullModel_ExceptionThrow(){

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
    void createCar_creatingCarWithNullMark_ExceptionThrow(){

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

    void createCar_creatingCarWithNullVinCode_ExceptionThrow(){

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
