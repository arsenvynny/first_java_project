package repository.impl;

import domain.Work;
import org.springframework.stereotype.Repository;
import repository.WorkRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryWorkRepository implements WorkRepository {
    private static long nextId = 1;
    private final Map<Long, Work> works = new HashMap<>();

    @Override
    public Work save(Work work) {
        if (work.getId() != null) {
            if (work.getId() >= nextId) {
                throw new IllegalStateException("Invalid work id");
            }
            works.put(work.getId(), work);
            return work;
        }

        work.setId(nextId++);
        works.put(work.getId(), work);
        return work;
    }

    @Override
    public Optional<Work> findById(Long id) {
        return Optional.ofNullable(works.get(id));
    }

    @Override
    public int getCount() {
        return works.size();
    }
}
