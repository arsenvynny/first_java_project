package domain.RepositoryTest;

import domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepairOrderRepository;
import repository.RepairOrderRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveTest {

    private RepairOrder order;
    private Car car;
    private Client client;

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        order = new RepairOrder(car, Priority.NORMAL);

    }

    @Test
    void save_saveOrder_isSaved(){

        RepairOrderRepository repository = new InMemoryRepairOrderRepository();

        repository.save(order);

        assertEquals(1L, order.getId());
        assertEquals(Optional.of(order), repository.findById(1L));
        assertEquals(1, repository.getCount());

    }

    @Test
    void save_ChangeAlreadySavedOrder_isSaved(){

        RepairOrderRepository repository = new InMemoryRepairOrderRepository();

        repository.save(order);

        Work work = new Work("wheels change",
                             "100",
                             "200",
                             Specialization.GENERAL_SERVICE);

        repository.save(order);

        assertEquals(2L, order.getId());
        assertEquals(Optional.of(order), repository.findById(2L));
        assertEquals(1, repository.getCount());

    }

    @Test
    void save_saveOrderWithInvalidId_ExceptionThrow(){

        RepairOrderRepository repository = new InMemoryRepairOrderRepository();

        order.setId(12L);

        assertThrows(IllegalStateException.class,
                () -> repository.save(order));
    }
}
