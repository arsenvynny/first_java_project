package domain.car;

import domain.Car;
import domain.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class CarTest {
    @Test
    void noArgsConstructorInitializesFieldsToDefaults() {
        Car car = new Car();

        assertNull(car.getId());
        assertNull(car.getModel());
        assertNull(car.getMark());
        assertEquals(0, car.getYear());
        assertNull(car.getOwner());
        assertNull(car.getVinCode());
    }

    @Test
    void allArgsConstructorAndAccessorsCoverEveryField() {
        Client owner = new Client(7L, "Ada", "Lovelace");
        Car car = new Car(1L, "Model S", "Tesla", 2024, owner, "VIN-123");

        assertEquals(1L, car.getId());
        assertEquals("Model S", car.getModel());
        assertEquals("Tesla", car.getMark());
        assertEquals(2024, car.getYear());
        assertSame(owner, car.getOwner());
        assertEquals("VIN-123", car.getVinCode());

        car.setId(2L);
        car.setModel("Roadster");
        car.setMark("Updated make");
        car.setYear(2025);
        car.setOwner(null);
        car.setVinCode("VIN-456");

        assertEquals(2L, car.getId());
        assertEquals("Roadster", car.getModel());
        assertEquals("Updated make", car.getMark());
        assertEquals(2025, car.getYear());
        assertNull(car.getOwner());
        assertEquals("VIN-456", car.getVinCode());
    }
}
