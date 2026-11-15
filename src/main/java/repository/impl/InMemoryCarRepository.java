package repository.impl;

import domain.Car;
import org.springframework.stereotype.Repository;
import repository.CarRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryCarRepository implements CarRepository {
    private static long nextId = 1;
    private final Map<Long, Car> cars = new HashMap<>();

    @Override
    public Car save(Car car) {
        if (car.getId() != null) {
            if (car.getId() >= nextId) {
                throw new IllegalStateException("Invalid car id");
            }
            cars.put(car.getId(), car);
            return car;
        }

        if (cars.containsValue(car)) {
            throw new IllegalStateException("Car already exists");
        }

        car.setId(nextId++);
        cars.put(car.getId(), car);
        return car;
    }

    @Override
    public Optional<Car> findById(Long id) {
        return Optional.ofNullable(cars.get(id));
    }

    @Override
    public int getCount() {
        return cars.size();
    }
}
