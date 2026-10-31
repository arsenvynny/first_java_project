package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepairOrderRepository;
import service.RepairOrderService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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
        repairOrderService = new RepairOrderService(repository);
    }

    @Test
    void createOrder_savesAndReturnsCreatedOrder() {
        Order order = repairOrderService.createOrder(car, Priority.NORMAL);

        assertSame(car, order.getCar());
        assertEquals(Priority.NORMAL, order.getPriority());
        assertEquals(1, order.getEvents().size());
        assertEquals(EventType.CREATED_ORDER, order.getEvents().get(0).getType());
        verify(repository).save(order);
    }
}
