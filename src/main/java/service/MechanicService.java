package service;

import domain.Mechanic;

import java.util.List;

public interface MechanicService {
    Mechanic create(Mechanic mechanic);
    Mechanic update(Mechanic mechanic);
    Mechanic findById(Long id);
    List<Mechanic> findAll();
    void delete(Long id);
}
