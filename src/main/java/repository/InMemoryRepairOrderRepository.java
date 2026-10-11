package repository;

import domain.Constants;
import domain.Order;
import domain.SnowflakeIdGenerator;

import java.util.*;

public class InMemoryRepairOrderRepository
        implements RepairOrderRepository {

    private SnowflakeIdGenerator generatorID = new SnowflakeIdGenerator(Constants.NodesID.FIRST);
    private Map<Long, Order> orders = new HashMap<>();

    @Override
    public void save(final Order order) {

        order.setID(generatorID.generateID());

        orders.put(order.getID(), order);
    }

    @Override
    public Optional<Order> findByID(final long ID) {
        return Optional.ofNullable(orders.get(ID));
    }
}
