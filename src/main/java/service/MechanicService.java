package service;

import domain.Mechanic;
import dto.MechanicDTO;

import java.util.List;

public interface MechanicService {
    Mechanic create(MechanicDTO mechanicDTO);
    Mechanic findById(Long id);
    List<Mechanic> findAll();
    Mechanic update(MechanicDTO mechanicDTO);
    void delete(Long id);
}
