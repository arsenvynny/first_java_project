package repository;

import domain.Order;

import java.util.Optional;

public interface RepairOrderRepository {
    Order save(final Order order);
    Optional<Order> findById(final Long ID);
    int getCount();
}
