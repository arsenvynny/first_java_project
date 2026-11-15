package domain.order;

import domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class OrderTest {

    @Test
    void Order_noArgsConstructor_initializesDefaultStatusAndEmptyCollections() {
        Order order = new Order();

        assertNull(order.getId());
        Assertions.assertEquals(OrderStatus.CREATED, order.getStatus());
        assertNull(order.getCar());
        assertNull(order.getMechanic());
        assertNull(order.getPriority());
        assertEquals(0, order.getWorks().size());
        assertEquals(0, order.getEvents().size());
    }

    @Test
    void Order_constructor_setsCarAndPriorityAndDefaultStatus() {
        Client client = new Client("Arsen", "Vynnytskyi");
        Car car = new Car("Toyota", "Camry", 2026, client, "BC0000AT");
        Order order = new Order(car, Priority.URGED);

        assertSame(car, order.getCar());
        assertEquals(Priority.URGED, order.getPriority());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(0, order.getWorks().size());
        assertEquals(0, order.getEvents().size());
    }

    @Test
    void Order_constructor_initializesIndependentWorkAndEventCollections() {
        Order first = new Order();
        Order second = new Order();

        assertNotSame(first.getWorks(), second.getWorks());
        assertNotSame(first.getEvents(), second.getEvents());
    }
}
