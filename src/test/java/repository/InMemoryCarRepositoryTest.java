package repository;

import domain.Car;
import domain.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.impl.InMemoryCarRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryCarRepositoryTest {

    private InMemoryCarRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCarRepository();
    }

    @Test
    void save_newCar_assignsIdAndMakesItFindable() {
        Car car = new Car("Toyota", "Camry", 2026,
                new Client("Arsen", "Vynnytskyi"), "BC0000AT");

        assertSame(car, repository.save(car));
        assertNotNull(car.getId());
        assertSame(car, repository.findById(car.getId()).orElseThrow());
        assertEquals(1, repository.getCount());
    }

    @Test
    void save_unallocatedId_ExceptionThrows() {
        Car car = new Car("Toyota", "Camry", 2026,
                new Client("Arsen", "Vynnytskyi"), "BC0000AT");
        car.setId(Long.MAX_VALUE);

        assertThrows(IllegalStateException.class, () -> repository.save(car));
    }
}
