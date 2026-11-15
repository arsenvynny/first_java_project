package service;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepairOrderRepository;
import service.impl.RepairServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateOrderTest {

    private RepairOrderRepository repository;
    private RepairServiceImpl repairOrderServiceImpl;
    private Car car;

    @BeforeEach
    void setUp() {
        Client client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        repository = mock(RepairOrderRepository.class);
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        repairOrderServiceImpl = new RepairServiceImpl(
                repository, RepairOrderServiceTestSupport.defaultPricePolicy());
    }

    @Test
    void createOrder_validCarAndPriority_savesAndReturnsCreatedOrder() {
        Order order = repairOrderServiceImpl.createOrder(car, Priority.NORMAL);

        assertSame(car, order.getCar());
        assertEquals(Priority.NORMAL, order.getPriority());
        assertEquals(1, order.getEvents().size());
        assertEquals(EventType.CREATED_ORDER, order.getEvents().getFirst().getType());
        verify(repository).save(order);
    }

    @Test
    void createOrder_nullCar_ExceptionThrows() {
        assertThrows(IllegalStateException.class,
                () -> repairOrderServiceImpl.createOrder(null, Priority.NORMAL));
    }

    @Test
    void createOrder_nullPriority_ExceptionThrows() {
        assertThrows(IllegalStateException.class,
                () -> repairOrderServiceImpl.createOrder(car, null));
    }

}
