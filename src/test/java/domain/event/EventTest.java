package domain.event;

import domain.Event;
import domain.EventType;
import domain.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class EventTest {
    @Test
    void noArgsConstructorInitializesFieldsToNull() {
        Event event = new Event();

        assertNull(event.getId());
        assertNull(event.getDescription());
        assertNull(event.getTime());
        assertNull(event.getType());
        assertNull(event.getOrder());
    }

    @Test
    void accessorsCoverEveryField() {
        Event event = new Event();
        Order order = new Order();
        LocalDateTime time = LocalDateTime.of(2026, 1, 2, 3, 4);

        event.setId(5L);
        event.setDescription("Order created");
        event.setTime(time);
        event.setType(EventType.CREATED_ORDER);
        event.setOrder(order);

        assertEquals(5L, event.getId());
        assertEquals("Order created", event.getDescription());
        assertEquals(time, event.getTime());
        assertEquals(EventType.CREATED_ORDER, event.getType());
        assertSame(order, event.getOrder());
    }
}
