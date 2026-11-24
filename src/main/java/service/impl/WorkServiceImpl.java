package service.impl;

import domain.Work;
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
    public Work create(Work work) {
        validate(work);
        if (work.getId() != null) {
            throw new IllegalArgumentException("A new work item must not already have an id");
        }
        work.setCompleted(false);
        work.setOrder(null);
        return repository.save(work);
    }

    @Override
    public Work update(Work work) {
        validate(work);
        if (work.getId() == null) {
            throw new IllegalArgumentException("A persisted work item must be specified for update");
        }
        Work existingWork = findById(work.getId());
        existingWork.setDescription(work.getDescription());
        existingWork.setWorkCost(work.getWorkCost());
        existingWork.setAutoPartsCost(work.getAutoPartsCost());
        existingWork.setSpecialization(work.getSpecialization());
        return repository.save(existingWork);
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
        repository.delete(findById(id));
    }

    private void validate(Work work) {
        if (work == null) {
            throw new IllegalArgumentException("Work item must be specified");
        }
        if (work.getDescription() == null || work.getDescription().isBlank()
                || work.getDescription().length() > 255) {
            throw new IllegalArgumentException("Work description must contain 1 to 255 characters");
        }
        validateCost(work.getWorkCost(), "Work cost");
        validateCost(work.getAutoPartsCost(), "Parts cost");
        if (work.getSpecialization() == null) {
            throw new IllegalArgumentException("A work specialization must be specified");
        }
    }

    private void validateCost(java.math.BigDecimal cost, String label) {
        if (cost == null || cost.signum() < 0 || cost.precision() > 12 || cost.scale() > 2) {
            throw new IllegalArgumentException(label + " must be non-negative with at most 10 integer and 2 decimal digits");
        }
    }
}
