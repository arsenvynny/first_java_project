package repository;

import domain.Order;

import java.util.Optional;

public interface RepairOrderRepository {
    public void save(final Order order);
    public Optional<Order> findByID(final String ID);
}
