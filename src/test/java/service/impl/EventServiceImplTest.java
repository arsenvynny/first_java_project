package service.impl;

import domain.Event;
import domain.EventType;
import domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    @Mock private EventRepository repository;
    private EventServiceImpl service;

    @BeforeEach
    void setUp() { service = new EventServiceImpl(repository); }

    @Test
    void recordCreatesTimestampedEvent() {
        Order order = new Order();
        order.setId(12L);
        LocalDateTime before = LocalDateTime.now();
        when(repository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event recorded = service.record(order, EventType.CREATED_ORDER, "Order opened");

        assertSame(order, recorded.getOrder());
        assertEquals(EventType.CREATED_ORDER, recorded.getType());
        assertEquals("Order opened", recorded.getDescription());
        assertNotNull(recorded.getTime());
        assertFalse(recorded.getTime().isBefore(before));
        assertFalse(recorded.getTime().isAfter(LocalDateTime.now()));
        verify(repository).save(recorded);
    }

    @Test
    void recordRejectsInvalidOrderTypeAndDescription() {
        assertInvalid(null, EventType.CREATED_ORDER, "valid");
        assertInvalid(new Order(), EventType.CREATED_ORDER, "valid");
        Order order = new Order();
        order.setId(1L);
        assertInvalid(order, null, "valid");
        assertInvalid(order, EventType.CREATED_ORDER, null);
        assertInvalid(order, EventType.CREATED_ORDER, " ");
        assertInvalid(order, EventType.CREATED_ORDER, "x".repeat(256));
        assertDoesNotThrow(() -> service.record(order, EventType.CREATED_ORDER, "x".repeat(255)));
    }

    @Test
    void findEventAndEventsForOrder() {
        Event event = new Event();
        when(repository.findById(3L)).thenReturn(Optional.of(event));
        when(repository.findByOrderIdOrderByTimeAscIdAsc(12L)).thenReturn(List.of(event));
        assertSame(event, service.findById(3L));
        assertEquals(List.of(event), service.findByOrderId(12L));

        when(repository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findById(4L));
    }

    private void assertInvalid(Order order, EventType type, String description) {
        assertThrows(IllegalArgumentException.class, () -> service.record(order, type, description));
    }
}
