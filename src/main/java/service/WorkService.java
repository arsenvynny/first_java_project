package service;

import domain.Work;
import dto.WorkDTO;

import java.util.List;

public interface WorkService {
    Work create(WorkDTO workDTO);
    Work update(WorkDTO workDTO);
    Work findById(Long id);
    List<Work> findAll();
    void delete(Long id);
}
