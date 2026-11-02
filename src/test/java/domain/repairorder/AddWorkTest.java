package domain.repairorder;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepairOrderRepository;
import service.RepairOrderService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;


class AddWorkTest {

    private Client client;
    private Car car;
    private Order order;
    private RepairOrderRepository repository;
    private RepairOrderService repairOrderService;

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        order = new Order(car, Priority.NORMAL);
        RepairOrderServiceTestSupport.Fixture fixture = RepairOrderServiceTestSupport.fixtureFor(order);
        repository = fixture.repository();
        repairOrderService = fixture.service();

    }

    @Test
    void addWork_addingWork_workIsAdded() {

        clearInvocations(repository);
        repairOrderService.diagnose(order);

        Work work = new Work("change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        repairOrderService.addWork(order, work);

        assertFalse(order.getWorks().isEmpty());
        verify(repository, org.mockito.Mockito.times(2)).save(order);
    }

    @Test
    void addWork_addingWorkWithoutDiagnose_ExceptionThrow() {

        Work work = new Work("change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        assertThrows(IllegalStateException.class, () -> repairOrderService.addWork(this.order, work));

    }

    @Test
    void addWork_addingNull_ExceptionThrow() {

        repairOrderService.diagnose(order);

        Work work = null;

        assertThrows(IllegalStateException.class, () -> repairOrderService.addWork(order, work));

    }

}