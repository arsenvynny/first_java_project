package domain.EventTest;

import domain.Event;
import domain.EventType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreatingEvent {

    @Test
    void Event_creatingEvent_isCreated() {

        String description = "Mechanic added";
        EventType eventType = EventType.APPROVED_ORDER;

        Event event = new Event(description, eventType);

        assertEquals(description, event.getDescription());
        assertEquals(eventType, event.getType());
    }

    @Test
    void Event_creatingEventWithInvalidDescription_ExceptionThrow() {

        String description = null;
        EventType eventType = EventType.APPROVED_ORDER;

        assertThrows(IllegalArgumentException.class,
                () -> new Event(description, eventType));

        String description2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Event(description2, eventType));
    }
}
