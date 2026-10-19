package repository;

import domain.RepairOrder;

import java.util.*;

public class InMemoryRepairOrderRepository
        implements RepairOrderRepository {
    private static long nextId = 1;
    private final Map<Long, RepairOrder> orders = new HashMap<>();

    @Override
    public Long getCount() {
        return nextId - 1;
    }

    @Override
    public void save(final RepairOrder newOrder) {
        if (newOrder.getID() != null) {
            if (newOrder.getID() >= nextId){
                throw new IllegalStateException("Invalid order id");
            }
            orders.put(newOrder.getID(), newOrder);
        }
        newOrder.setId(nextId++);
        orders.put(newOrder.getID(), newOrder);

    }

    @Override
    public Optional<RepairOrder> findById(final Long ID) {
        return Optional.ofNullable(orders.get(ID));
    }
}
