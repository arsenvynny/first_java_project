package service.impl;

import domain.Work;
import dto.WorkDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.WorkRepository;
import service.WorkService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkServiceImpl implements WorkService {
    private final WorkRepository repository;

    @Override
    public Work create(WorkDTO workDTO) {
        throw new UnsupportedOperationException("WorkDTO does not contain work fields");
    }

    @Override
    public Work update(WorkDTO workDTO) {
        throw new UnsupportedOperationException("WorkDTO does not contain work fields");
    }

    @Override
    @Transactional(readOnly = true)
    public Work findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Work with id " + id + " does not exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Work> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
