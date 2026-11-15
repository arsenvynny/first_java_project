package repository;

import domain.Work;

import java.util.Optional;

public interface WorkRepository {
    Work save(Work work);
    Optional<Work> findById(Long id);
    int getCount();
}
