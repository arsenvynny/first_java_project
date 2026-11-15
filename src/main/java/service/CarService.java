package service;

import domain.Car;
import dto.CarDTO;

import java.util.List;

public interface CarService {
    Car create(CarDTO carDTO);
    Car findById(Long id);
    List<Car> findAll();
    Car update(CarDTO carDTO);
    void delete(Long id);
}
