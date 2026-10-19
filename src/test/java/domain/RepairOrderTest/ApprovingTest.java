package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepairOrderRepository;
import repository.RepairOrderRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class ApprovingTest {

    private Client client;
    private Car car;
    private RepairOrder order;

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        order = new RepairOrder(car, Priority.NORMAL);

        order.diagnose();
    }

    @Test
    void approve_approvingOrder_isApproved(){

        Work work1 = new Work("Change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                "100",
                "4000",
                Specialization.ELECTRICIAN);

        order.addWork(work1);
        order.addWork(work1);

        order.approve();

        assertEquals(OrderStatus.APPROVED, order.getStatus());
    }

    @Test
    void approve_approvingOrderWithoutWorks_isApproved(){

        assertThrows(IllegalStateException.class, () -> order.approve());
    }
}
