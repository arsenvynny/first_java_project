package domain.order;

import domain.Car;
import domain.Event;
import domain.Mechanic;
import domain.Order;
import domain.OrderStatus;
import domain.Priority;
import domain.Work;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {
    @Test
    void noArgsConstructorSetsDefaultStatusAndIndependentCollections() {
        Order order = new Order();
        Order anotherOrder = new Order();

        assertNull(order.getId());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertNull(order.getCar());
        assertNotNull(order.getWorks());
        assertTrue(order.getWorks().isEmpty());
        assertNull(order.getMechanic());
        assertNull(order.getPriority());
        assertNotNull(order.getEvents());
        assertTrue(order.getEvents().isEmpty());
        assertNull(order.getFinalCost());
        assertNull(order.getCompletedAt());
        assertNull(order.getVersion());
        assertNotSame(order.getWorks(), anotherOrder.getWorks());
        assertNotSame(order.getEvents(), anotherOrder.getEvents());
    }

    @Test
    void accessorsCoverEveryField() {
        Order order = new Order();
        Car car = new Car();
        Mechanic mechanic = new Mechanic();
        Work work = new Work();
        Event event = new Event();
        List<Work> works = new ArrayList<>(List.of(work));
        List<Event> events = new ArrayList<>(List.of(event));
        LocalDateTime completedAt = LocalDateTime.of(2026, 2, 3, 4, 5);
        BigDecimal finalCost = new BigDecimal("123.45");

        order.setId(9L);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setCar(car);
        order.setWorks(works);
        order.setMechanic(mechanic);
        order.setPriority(Priority.URGED);
        order.setEvents(events);
        order.setFinalCost(finalCost);
        order.setCompletedAt(completedAt);
        order.setVersion(2L);

        assertEquals(9L, order.getId());
        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
        assertSame(car, order.getCar());
        assertSame(works, order.getWorks());
        assertSame(mechanic, order.getMechanic());
        assertEquals(Priority.URGED, order.getPriority());
        assertSame(events, order.getEvents());
        assertSame(finalCost, order.getFinalCost());
        assertEquals(completedAt, order.getCompletedAt());
        assertEquals(2L, order.getVersion());
    }
}
