package domain.RepairOrderTest;

import domain.*;
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
import static org.mockito.ArgumentMatchers.any;

public class makeInProgressTest {

    private Client client;
    private Car car;
    private RepairOrder order;
    private List<Specialization> specializations = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        order = new RepairOrder(car, Priority.NORMAL);

        Work work1 = new Work("Change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                "100",
                "4000",
                Specialization.ELECTRICIAN);

        order.diagnose();
        order.addWork(work1);
        order.addWork(work2);

        Specialization specialization1 = Specialization.GENERAL_SERVICE;
        Specialization specialization2 = Specialization.ELECTRICIAN;

        specializations.add(specialization1);
        specializations.add(specialization2);

        order.approve();
    }

    @Test
    void makeInProgress_makeOrderInProgress_isInProgress(){

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        order.assignMechanic(mechanic);
        order.makeInProgress();

        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
    }

    @Test
    void makeInProgress_TryToMakeInProgressWithNullMechanic_ExceptionThrow(){

        Mechanic mechanic = null;

        assertThrows(IllegalStateException.class, () -> order.makeInProgress());
    }
}
