package repository;

import domain.RepairOrder;

import java.util.Optional;

public interface RepairOrderRepository {
    void save(final RepairOrder order);
    Optional<RepairOrder> findById(final Long ID);
    int getCount();
}
