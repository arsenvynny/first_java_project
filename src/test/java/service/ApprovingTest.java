package service;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepairOrderRepository;
import service.impl.RepairServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

public class ApprovingTest {

    private Client client;
    private Car car;
    private Order order;
    private RepairOrderRepository repository;
    private RepairServiceImpl repairOrderServiceImpl;

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        order = new Order(car, Priority.NORMAL);
        RepairOrderServiceTestSupport.Fixture fixture = RepairOrderServiceTestSupport.fixtureFor(order);
        repository = fixture.repository();
        repairOrderServiceImpl = fixture.service();

        repairOrderServiceImpl.diagnose(order);
    }

    @Test
    void approve_diagnosedOrder_changesStatusToApproved() {

        Work work1 = new Work("Change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                "100",
                "4000",
                Specialization.ELECTRICIAN);

        repairOrderServiceImpl.addWork(order, work1);
        repairOrderServiceImpl.addWork(order, work1);

        clearInvocations(repository);
        repairOrderServiceImpl.approve(order);

        assertEquals(OrderStatus.APPROVED, order.getStatus());
        verify(repository).save(order);
    }

    @Test
    void approve_orderWithoutWorks_changesStatusToApproved() {

        assertThrows(IllegalStateException.class, () -> repairOrderServiceImpl.approve(order));
    }
}
