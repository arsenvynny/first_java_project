package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class makeInProgressTest {

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
    }

    @Test
    void makeInProgress_makeOrderInProgress_isInProgress() {

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        repairOrderService.assignMechanic(order, mechanic);
        repairOrderService.makeInProgress(order);

        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
    }

    @Test
    void makeInProgress_TryToMakeInProgressWithNullMechanic_ExceptionThrow() {

        Mechanic mechanic = null;

        assertThrows(IllegalStateException.class, () -> repairOrderService.makeInProgress(order));
    }
}
