package repository;

import domain.RepairOrder;

import java.util.*;

public class InMemoryRepairOrderRepository
        implements RepairOrderRepository {
    private static long nextId = 1;
    private final Map<Long, RepairOrder> orders = new HashMap<>();

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public void save(final RepairOrder newOrder) {
        if (newOrder.getId() != null) {
            if (newOrder.getId() >= nextId){
                throw new IllegalStateException("Invalid order id");
            }
            orders.put(newOrder.getId(), newOrder);
            return;
        }
        newOrder.setId(nextId++);
        orders.put(newOrder.getId(), newOrder);

    }

    @Override
    public Optional<RepairOrder> findById(final Long ID) {
        return Optional.ofNullable(orders.get(ID));
    }
}
