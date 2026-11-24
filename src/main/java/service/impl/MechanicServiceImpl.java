package service.impl;

import domain.Mechanic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.MechanicRepository;
import service.MechanicService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MechanicServiceImpl implements MechanicService {
    private final MechanicRepository repository;

    @Override
    public Mechanic create(Mechanic mechanic) {
        validate(mechanic);
        if (mechanic.getId() != null) {
            throw new IllegalArgumentException("A new mechanic must not already have an id");
        }
        mechanic.setAvailable(true);
        return repository.save(mechanic);
    }

    @Override
    public Mechanic update(Mechanic mechanic) {
        validate(mechanic);
        if (mechanic.getId() == null) {
            throw new IllegalArgumentException("A persisted mechanic must be specified for update");
        }
        Mechanic existing = findById(mechanic.getId());
        existing.setName(mechanic.getName());
        existing.setSpecializationList(mechanic.getSpecializationList());
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Mechanic findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mechanic with id " + id + " does not exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mechanic> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Mechanic mechanic) {
        if (mechanic == null) {
            throw new IllegalArgumentException("Mechanic must be specified");
        }
        if (mechanic.getName() == null || mechanic.getName().isBlank() || mechanic.getName().length() > 50) {
            throw new IllegalArgumentException("Mechanic name must contain 1 to 50 characters");
        }
        if (mechanic.getSpecializationList() == null || mechanic.getSpecializationList().isEmpty()
                || mechanic.getSpecializationList().stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException("At least one valid specialization must be specified");
        }
        if (mechanic.getSpecializationList().stream().distinct().count()
                != mechanic.getSpecializationList().size()) {
            throw new IllegalArgumentException("Mechanic specializations must be unique");
        }
    }
}
