package service.impl;

import domain.Event;
import domain.EventType;
import domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.EventRepository;
import service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository repository;

    @Override
    public Event record(Order order, EventType type, String description) {
        validate(order, type, description);
        Event event = new Event();
        event.setOrder(order);
        event.setType(type);
        event.setDescription(description);
        event.setTime(LocalDateTime.now());
        return repository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Event findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event with id " + id + " does not exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findByOrderId(Long orderId) {
        return repository.findByOrderIdOrderByTimeAscIdAsc(orderId);
    }

    private void validate(Order order, EventType type, String description) {
        if (order == null || order.getId() == null)
            throw new IllegalArgumentException("A persisted order must be specified for the event");
        if (type == null)
            throw new IllegalArgumentException("The event type cannot be null");
        if (description == null || description.isBlank() || description.length() > 255)
            throw new IllegalArgumentException("Event description must contain 1 to 255 characters");
    }
}
