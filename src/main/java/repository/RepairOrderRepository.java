package repository;

import domain.Car;
import domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepairOrderRepository extends JpaRepository<Order, Long> {
    Order save(final Order order);
    Optional<Order> findById(final Long ID);

}
