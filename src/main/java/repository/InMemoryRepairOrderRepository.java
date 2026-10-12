package repository;

import domain.Order;

import java.util.*;

public class InMemoryRepairOrderRepository
        implements RepairOrderRepository {
    private Map<String, Order> orders = new HashMap<>();

    @Override
    public void save(final Order newOrder) {

        if (newOrder.getID() != null) {
            orders.put(newOrder.getID(), newOrder);
        } else {

            newOrder.assignId(UUID.randomUUID().toString());

            orders.put(newOrder.getID(), newOrder);
        }
    }

    @Override
    public Optional<Order> findByID(final String ID) {
        return Optional.ofNullable(orders.get(ID));
    }

}
