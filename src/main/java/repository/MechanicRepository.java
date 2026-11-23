package repository;

import domain.Car;
import domain.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    Mechanic save(Mechanic mechanic);
    Optional<Mechanic> findById(Long id);

}
