package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiagnosingTest {

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
    void diagnose_MakeDiagnostic_isDiagnosed() {

        repairOrderService.diagnose(order);

        assertEquals(OrderStatus.DIAGNOSED, order.getStatus());
    }

    @Test
    void diagnose_diagnoseCanceledOrder_ExceptionThrow() {

        repairOrderService.cancel(order);

        assertThrows(IllegalStateException.class, () -> repairOrderService.diagnose(order));
    }
}
