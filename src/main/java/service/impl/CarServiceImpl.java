package service.impl;

import domain.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CarRepository;
import repository.ClientRepository;
import service.CarService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    @Override
    public Car create(Car car) {
        validate(car);
        if (car.getId() != null) {
            throw new IllegalArgumentException("A new car must not already have an id");
        }
        car.setOwner(clientRepository.findById(car.getOwner().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Client with id " + car.getOwner().getId() + " does not exist")));
        return carRepository.save(car);
    }

    @Override
    @Transactional(readOnly = true)
    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car with id " + id + " does not exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Car update(Car car) {
        validate(car);
        if (car.getId() == null || !carRepository.existsById(car.getId())) {
            throw new IllegalArgumentException("A persisted car must be specified for update");
        }
        car.setOwner(clientRepository.findById(car.getOwner().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Client with id " + car.getOwner().getId() + " does not exist")));
        return carRepository.save(car);
    }

    @Override
    public void delete(Long id) {
        Car car = findById(id);
        carRepository.delete(car);
    }

    private void validate(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car must be specified");
        }
        if (car.getModel() == null || car.getModel().isBlank() || car.getModel().length() > 50) {
            throw new IllegalArgumentException("Car model must contain 1 to 50 characters");
        }
        if (car.getMark() == null || car.getMark().isBlank() || car.getMark().length() > 50) {
            throw new IllegalArgumentException("Car mark must contain 1 to 50 characters");
        }
        if (car.getOwner() == null || car.getOwner().getId() == null) {
            throw new IllegalArgumentException("A persisted client must be specified as the car owner");
        }
        if (car.getVinCode() == null || car.getVinCode().isBlank() || car.getVinCode().length() != 17) {
            throw new IllegalArgumentException("VIN code must contain 17 characters");
        }
    }
}
