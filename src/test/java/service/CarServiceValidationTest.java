package service;

import domain.Car;
import org.junit.jupiter.api.Test;
import service.impl.CarServiceImpl;
import testutil.EntityFixtures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarServiceValidationTest {

    @Test
    void createCar_validInput_setsCarFields() {
        var owner = EntityFixtures.client();
        Car car = new CarServiceImpl().createCar("Toyota", "Camry", 2026, owner, "BC0000AT");

        assertEquals("Toyota", car.getMark());
        assertEquals("Camry", car.getModel());
        assertEquals(2026, car.getYear());
        assertEquals("BC0000AT", car.getVinCode());
        assertEquals(owner, car.getOwner());
    }

    @Test
    void createCar_nullOrEmptyModel_throwsIllegalArgumentException() {
        var service = new CarServiceImpl();
        assertThrows(IllegalArgumentException.class,
                () -> service.createCar("Toyota", null, 2026, EntityFixtures.client(), "BC0000AT"));
        assertThrows(IllegalArgumentException.class,
                () -> service.createCar("Toyota", "", 2026, EntityFixtures.client(), "BC0000AT"));
    }

    @Test
    void createCar_nullOrEmptyMark_throwsIllegalArgumentException() {
        var service = new CarServiceImpl();
        assertThrows(IllegalArgumentException.class,
                () -> service.createCar(null, "Camry", 2026, EntityFixtures.client(), "BC0000AT"));
        assertThrows(IllegalArgumentException.class,
                () -> service.createCar("", "Camry", 2026, EntityFixtures.client(), "BC0000AT"));
    }

    @Test
    void createCar_nullOrEmptyVinCode_throwsIllegalArgumentException() {
        var service = new CarServiceImpl();
        assertThrows(IllegalArgumentException.class,
                () -> service.createCar("Toyota", "Camry", 2026, EntityFixtures.client(), null));
        assertThrows(IllegalArgumentException.class,
                () -> service.createCar("Toyota", "Camry", 2026, EntityFixtures.client(), ""));
    }
}
