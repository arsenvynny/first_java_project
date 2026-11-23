package service.impl;

import domain.Car;
import dto.CarDTO;
import org.springframework.stereotype.Service;
import service.CarService;

import java.util.List;

@Service
public class ClientServiceImpl implements CarService {
    @Override
    public Car create(CarDTO carDTO) {
        return null;
    }

    @Override
    public Car findById(Long id) {
        return null;
    }

    @Override
    public List<Car> findAll() {
        return List.of();
    }

    @Override
    public Car update(CarDTO carDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
