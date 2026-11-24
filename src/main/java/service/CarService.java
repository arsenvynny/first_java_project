package service;

import domain.Car;

import java.util.List;

public interface CarService {
    Car create(Car car);
    Car findById(Long id);
    List<Car> findAll();
    Car update(Car car);
    void delete(Long id);
}
