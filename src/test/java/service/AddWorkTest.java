package service;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepairOrderRepository;
import service.impl.RepairServiceImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;


class AddWorkTest {

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

    }

    @Test
    void addWork_validWork_addsWorkToOrder() {

        clearInvocations(repository);
        repairOrderServiceImpl.diagnose(order);

        Work work = new Work("change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        repairOrderServiceImpl.addWork(order, work);

        assertFalse(order.getWorks().isEmpty());
        verify(repository, org.mockito.Mockito.times(2)).save(order);
    }

    @Test
    void addWork_undiagnosedOrder_ExceptionThrows() {

        Work work = new Work("change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        assertThrows(IllegalStateException.class, () -> repairOrderServiceImpl.addWork(this.order, work));

    }

    @Test
    void addWork_nullWork_ExceptionThrows() {

        repairOrderServiceImpl.diagnose(order);

        Work work = null;

        assertThrows(IllegalStateException.class, () -> repairOrderServiceImpl.addWork(order, work));

    }

}