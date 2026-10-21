package domain.RepositoryTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SaveTest {

    private service.RepairOrderService repairOrderService;
    private Car car;

    @BeforeEach
    public void setUp() {

        Client client = new Client("Arsen", "Vynnytskyi");
        car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");

        repairOrderService = new service.RepairOrderService(new repository.InMemoryRepairOrderRepository());
    }

    @Test
    void createOrder_createdOrder_isSaved(){
        Order order = repairOrderService.createOrder(car, Priority.NORMAL);

        assertNotNull(order.getId());
        assertEquals(Optional.of(order), repairOrderService.findOrderById(order.getId()));
        assertEquals(1, repairOrderService.getOrderCount());
    }

    @Test
    void createOrder_multipleOrders_areSavedWithUniqueIds(){
        Order firstOrder = repairOrderService.createOrder(car, Priority.NORMAL);
        Order secondOrder = repairOrderService.createOrder(car, Priority.NORMAL);

        assertNotEquals(firstOrder.getId(), secondOrder.getId());
        assertEquals(Optional.of(firstOrder), repairOrderService.findOrderById(firstOrder.getId()));
        assertEquals(Optional.of(secondOrder), repairOrderService.findOrderById(secondOrder.getId()));
        assertEquals(2, repairOrderService.getOrderCount());
    }
}
