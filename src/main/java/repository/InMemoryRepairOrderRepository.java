package repository;

import domain.Order;

import java.util.*;

public class InMemoryRepairOrderRepository
        implements RepairOrderRepository {
    private static long nextId = 1;
    private final Map<Long, Order> orders = new HashMap<>();

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Order save(final Order newOrder) {
        if (newOrder.getId() != null) {
            if (newOrder.getId() >= nextId){
                throw new IllegalStateException("Invalid order id");
            }
            orders.put(newOrder.getId(), newOrder);
            return newOrder;
        }

        newOrder.setId(nextId++);
        orders.put(newOrder.getId(), newOrder);

        return newOrder;
    }

    @Override
    public Optional<Order> findById(final Long ID) {
        return Optional.ofNullable(orders.get(ID));
    }
}
