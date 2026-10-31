package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompleteTest {

    private Client client;
    private Car car;
    private Order order;
    private service.RepairOrderService repairOrderService;
    private List<Specialization> specializations = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        repairOrderService = new service.RepairOrderService(
                org.mockito.Mockito.mock(repository.RepairOrderRepository.class));

        order = new Order(car, Priority.NORMAL);

        Work work1 = new Work("Change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                "100",
                "4000",
                Specialization.ELECTRICIAN);

        repairOrderService.diagnose(order);
        repairOrderService.addWork(order, work1);
        repairOrderService.addWork(order, work2);

        Specialization specialization1 = Specialization.GENERAL_SERVICE;
        Specialization specialization2 = Specialization.ELECTRICIAN;

        specializations.add(specialization1);
        specializations.add(specialization2);

        repairOrderService.approve(order);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        repairOrderService.assignMechanic(order, mechanic);
    }

    @Test
    void complete_completeOrder_isCompleted_isMechanicFree() {

        repairOrderService.makeInProgress(order);

        for (Work work : order.getWorks()) {
            work.makeDone();
        }

        repairOrderService.complete(order);

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    void complete_completeOrderWithOutDoneWorks_ExceptionThrow() {

        repairOrderService.makeInProgress(order);

        assertThrows(IllegalStateException.class, () -> repairOrderService.complete(order));

        assertFalse(order.getMechanic().isAvailable());
    }

    @Test
    void complete_completeNonInProgressOrder_ExceptionThrow() {

        for (Work work : order.getWorks()) {
            work.makeDone();
        }

        assertThrows(IllegalStateException.class, () -> repairOrderService.complete(order));

        assertFalse(order.getMechanic().isAvailable());
    }
}
