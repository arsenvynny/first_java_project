package service.impl;

import domain.Mechanic;
import dto.MechanicDTO;
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
    public Mechanic create(MechanicDTO mechanicDTO) {
        throw new UnsupportedOperationException("MechanicDTO does not contain mechanic fields");
    }

    @Override
    public Mechanic update(MechanicDTO mechanicDTO) {
        throw new UnsupportedOperationException("MechanicDTO does not contain mechanic fields");
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
        repository.deleteById(id);
    }
}
