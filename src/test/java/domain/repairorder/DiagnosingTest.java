package domain.repairorder;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepairOrderRepository;
import service.RepairOrderService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DiagnosingTest {

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
        order.setId(1L);
        repository = mock(RepairOrderRepository.class);
        when(repository.findById(order.getId())).thenReturn(Optional.of(order));
        when(repository.save(order)).thenReturn(order);
        repairOrderService = new RepairOrderService(
                repository, RepairOrderServiceTestSupport.defaultPricePolicy());
    }

    @Test
    void diagnose_MakeDiagnostic_isDiagnosed() {

        repairOrderService.diagnose(order);

        assertEquals(OrderStatus.DIAGNOSED, order.getStatus());
        verify(repository).save(order);
    }

    @Test
    void diagnose_diagnoseCanceledOrder_ExceptionThrow() {

        repairOrderService.cancel(order);

        assertThrows(IllegalStateException.class, () -> repairOrderService.diagnose(order));
    }

    @Test
    void diagnose_orderDoesNotExist_throwsException() {
        Order missingOrder = new Order(car, Priority.NORMAL);
        missingOrder.setId(2L);

        assertThrows(IllegalArgumentException.class, () -> repairOrderService.diagnose(missingOrder));
    }
}
