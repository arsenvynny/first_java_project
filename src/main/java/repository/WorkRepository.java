package repository;

import domain.Car;
import domain.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Long> {
    Work save(Work work);
    Optional<Work> findById(Long id);
}
