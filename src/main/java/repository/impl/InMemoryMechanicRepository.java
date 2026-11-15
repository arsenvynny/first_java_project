package repository.impl;

import domain.Mechanic;
import org.springframework.stereotype.Repository;
import repository.MechanicRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryMechanicRepository implements MechanicRepository {
    private static long nextId = 1;
    private final Map<Long, Mechanic> mechanics = new HashMap<>();

    @Override
    public Mechanic save(Mechanic mechanic) {
        if (mechanic.getId() != null) {
            if (mechanic.getId() >= nextId) {
                throw new IllegalStateException("Invalid mechanic id");
            }
            mechanics.put(mechanic.getId(), mechanic);
            return mechanic;
        }

        mechanic.setId(nextId++);
        mechanics.put(mechanic.getId(), mechanic);
        return mechanic;
    }

    @Override
    public Optional<Mechanic> findById(Long id) {
        return Optional.ofNullable(mechanics.get(id));
    }

    @Override
    public int getCount() {
        return mechanics.size();
    }
}
