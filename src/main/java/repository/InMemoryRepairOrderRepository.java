package repository;

import domain.Constants;
import domain.Order;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryRepairOrderRepository
        implements RepairOrderRepository {
    private UUID id;
    private Map<String, Order> orders = new HashMap<>();

    @Override
    public void save(final Order newOrder) {

        if (newOrder.getID() != null) {
            orders.replace(newOrder.getID(), orders.get(newOrder.getID()), newOrder);
        } else {

            newOrder.setID(UUID.randomUUID().toString());

            orders.put(newOrder.getID(), newOrder);
        }
    }

    @Override
    public Optional<Order> findByID(final String ID) {
        return Optional.ofNullable(orders.get(ID));
    }

}
