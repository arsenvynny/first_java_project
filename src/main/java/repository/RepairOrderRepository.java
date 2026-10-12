package repository;

import domain.RepairOrder;

import java.util.Optional;

public interface RepairOrderRepository {
    public void save(final RepairOrder order);
    public Optional<RepairOrder> findByID(final String ID);
}
