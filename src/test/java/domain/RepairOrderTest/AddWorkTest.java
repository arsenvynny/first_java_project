package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AddWorkTest {

    private Client client;
    private Car car;
    private Order order;
    private service.RepairOrderService repairOrderService;

    @BeforeEach
    public void setUp() {

        client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        repairOrderService = new service.RepairOrderService(
                org.mockito.Mockito.mock(repository.RepairOrderRepository.class));

        order = new Order(car, Priority.NORMAL);

    }

    @Test
    void addWork_addingWork_workIsAdded() {

        repairOrderService.diagnose(order);

        Work work = new Work("change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        repairOrderService.addWork(order, work);

        assertFalse(order.getWorks().isEmpty());
    }

    @Test
    void addWork_addingWorkWithoutDiagnose_ExceptionThrow() {

        Order order = new Order(car, Priority.NORMAL);

        Work work = new Work("change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        assertThrows(IllegalStateException.class, () -> repairOrderService.addWork(order, work));

    }

    @Test
    void addWork_addingNull_ExceptionThrow() {

        Order order = new Order(car, Priority.NORMAL);

        Work work = null;

        assertThrows(IllegalStateException.class, () -> repairOrderService.addWork(order, work));

    }

}