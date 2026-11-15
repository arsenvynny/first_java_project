package repository;

import domain.Car;

import java.util.Optional;

public interface CarRepository {
    Car save(Car car);
    Optional<Car> findById(Long id);
    int getCount();
}
