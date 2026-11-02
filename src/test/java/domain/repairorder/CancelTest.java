package domain.repairorder;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepairOrderRepository;
import service.RepairOrderService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

public class CancelTest {

    private Client client;
    private Car car;
    private Order order;
    private RepairOrderRepository repository;
    private RepairOrderService repairOrderService;
    private List<Specialization> specializations = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        order = new Order(car, Priority.NORMAL);
        RepairOrderServiceTestSupport.Fixture fixture = RepairOrderServiceTestSupport.fixtureFor(order);
        repository = fixture.repository();
        repairOrderService = fixture.service();

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

        repairOrderService.makeInProgress(order);
    }

    @Test
    void cancel_cancelOrder_isCanceled() {

        clearInvocations(repository);
        repairOrderService.cancel(order);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(repository).save(order);
    }

    @Test
    void cancel_cancelCompletedOrder_ExceptionThrow() {

        for (Work work : order.getWorks()) {
            work.makeDone();
        }

        repairOrderService.complete(order);

        assertThrows(IllegalStateException.class, () -> repairOrderService.cancel(order));
    }

    @Test
    void cancel_cancelCanceledOrder_ExceptionThrow() {

        repairOrderService.cancel(order);

        assertThrows(IllegalStateException.class, () -> repairOrderService.cancel(order));
    }

}
