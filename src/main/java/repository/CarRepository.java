package repository;

import domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car save(Car car);
    Optional<Car> findById(Long id);
}
