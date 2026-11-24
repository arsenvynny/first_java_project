package service;

import domain.Work;

import java.util.List;

public interface WorkService {
    Work create(Work work);
    Work update(Work work);
    Work findById(Long id);
    List<Work> findAll();
    void delete(Long id);
}
