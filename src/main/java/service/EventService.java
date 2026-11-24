package service;

import domain.Event;
import domain.EventType;
import domain.Order;

import java.util.List;

public interface EventService {
    Event record(Order order, EventType type, String description);
    Event findById(Long id);
    List<Event> findByOrderId(Long orderId);
}
