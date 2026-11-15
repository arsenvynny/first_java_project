package service;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepairOrderRepository;
import service.impl.RepairServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

public class CompleteTest {

    private Client client;
    private Car car;
    private Order order;
    private RepairOrderRepository repository;
    private RepairServiceImpl repairOrderServiceImpl;
    private List<Specialization> specializations = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        order = new Order(car, Priority.NORMAL);
        RepairOrderServiceTestSupport.Fixture fixture = RepairOrderServiceTestSupport.fixtureFor(order);
        repository = fixture.repository();
        repairOrderServiceImpl = fixture.service();

        Work work1 = new Work("Change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                "100",
                "4000",
                Specialization.ELECTRICIAN);

        repairOrderServiceImpl.diagnose(order);
        repairOrderServiceImpl.addWork(order, work1);
        repairOrderServiceImpl.addWork(order, work2);

        Specialization specialization1 = Specialization.GENERAL_SERVICE;
        Specialization specialization2 = Specialization.ELECTRICIAN;

        specializations.add(specialization1);
        specializations.add(specialization2);

        repairOrderServiceImpl.approve(order);

        Mechanic mechanic = new Mechanic("Arsen", specializations);

        repairOrderServiceImpl.assignMechanic(order, mechanic);
    }

    @Test
    void complete_inProgressOrder_changesStatusAndFreesMechanic() {

        repairOrderServiceImpl.makeInProgress(order);

        for (Work work : order.getWorks()) {
            work.makeDone();
        }

        clearInvocations(repository);
        repairOrderServiceImpl.complete(order);

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        verify(repository).save(order);
    }

    @Test
    void complete_orderWithIncompleteWorks_ExceptionThrows() {

        repairOrderServiceImpl.makeInProgress(order);

        assertThrows(IllegalStateException.class, () -> repairOrderServiceImpl.complete(order));

        assertFalse(order.getMechanic().isAvailable());
    }

    @Test
    void complete_orderNotInProgress_ExceptionThrows() {

        for (Work work : order.getWorks()) {
            work.makeDone();
        }

        assertThrows(IllegalStateException.class, () -> repairOrderServiceImpl.complete(order));

        assertFalse(order.getMechanic().isAvailable());
    }
}
