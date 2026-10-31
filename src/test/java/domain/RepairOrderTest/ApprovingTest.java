package domain.RepairOrderTest;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApprovingTest {

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

        repairOrderService.diagnose(order);
    }

    @Test
    void approve_approvingOrder_isApproved() {

        Work work1 = new Work("Change wheels",
                "100",
                "4000",
                Specialization.GENERAL_SERVICE);

        Work work2 = new Work("fix accumulator",
                "100",
                "4000",
                Specialization.ELECTRICIAN);

        repairOrderService.addWork(order, work1);
        repairOrderService.addWork(order, work1);

        repairOrderService.approve(order);

        assertEquals(OrderStatus.APPROVED, order.getStatus());
    }

    @Test
    void approve_approvingOrderWithoutWorks_isApproved() {

        assertThrows(IllegalStateException.class, () -> repairOrderService.approve(order));
    }
}
