package domain.repairorder;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepairOrderRepository;
import service.RepairOrderService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateOrderTest {

    private RepairOrderRepository repository;
    private RepairOrderService repairOrderService;
    private Car car;

    @BeforeEach
    void setUp() {
        Client client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        repository = mock(RepairOrderRepository.class);
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        repairOrderService = new RepairOrderService(
                repository, RepairOrderServiceTestSupport.defaultPricePolicy());
    }

    @Test
    void createOrder_savesAndReturnsCreatedOrder_isSavedIsCreated() {
        Order order = repairOrderService.createOrder(car, Priority.NORMAL);

        assertSame(car, order.getCar());
        assertEquals(Priority.NORMAL, order.getPriority());
        assertEquals(1, order.getEvents().size());
        assertEquals(EventType.CREATED_ORDER, order.getEvents().getFirst().getType());
        verify(repository).save(order);
    }

    @Test
    void createOrder_createOrderWithNullCar_ExceptionThrow() {
        assertThrows(IllegalStateException.class,
                () -> repairOrderService.createOrder(null, Priority.NORMAL));
    }

    @Test
    void createOrder_createOrderWithNullPriority_ExceptionThrow() {
        assertThrows(IllegalStateException.class,
                () -> repairOrderService.createOrder(car, null));
    }

}
