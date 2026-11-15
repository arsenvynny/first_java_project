package repository;

import domain.Mechanic;

import java.util.Optional;

public interface MechanicRepository {
    Mechanic save(Mechanic mechanic);
    Optional<Mechanic> findById(Long id);
    int getCount();
}
