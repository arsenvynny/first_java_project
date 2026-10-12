package repository;

import domain.RepairOrder;

import java.util.*;

public class InMemoryRepairOrderRepository
        implements RepairOrderRepository {
    private Map<String, RepairOrder> orders = new HashMap<>();

    @Override
    public void save(final RepairOrder newOrder) {

        if (newOrder.getID() != null) {
            orders.put(newOrder.getID(), newOrder);
        } else {

            newOrder.assignId(UUID.randomUUID().toString());

            orders.put(newOrder.getID(), newOrder);
        }
    }

    @Override
    public Optional<RepairOrder> findByID(final String ID) {
        return Optional.ofNullable(orders.get(ID));
    }

}
