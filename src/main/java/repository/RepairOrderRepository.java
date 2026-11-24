package repository;


import domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RepairOrderRepository extends JpaRepository<Order, Long> {
}
