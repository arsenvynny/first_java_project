package service.impl;

import domain.Car;
import domain.Order;
import domain.OrderStatus;
import domain.Work;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CarRepository;
import repository.RepairOrderRepository;
import repository.WorkRepository;
import service.OrderService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final RepairOrderRepository orderRepository;
    private final CarRepository carRepository;
    private final WorkRepository workRepository;

    @Override
    public Order create(Order order) {
        validate(order);
        if (order.getId() != null) {
            throw new IllegalArgumentException("A new order must not already have an id");
        }
        if (order.getMechanic() != null) {
            throw new IllegalArgumentException("Assign a mechanic through the repair service after order approval");
        }
        if (order.getWorks() != null && !order.getWorks().isEmpty()) {
            throw new IllegalArgumentException("Add work items through the repair service after diagnosis");
        }

        order.setCar(findCar(order.getCar()));
        order.setStatus(OrderStatus.CREATED);
        order.setWorks(new ArrayList<>());

        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        validate(order);
        if (order.getId() == null) {
            throw new IllegalArgumentException("A persisted order must be specified for update");
        }

        Order existing = findById(order.getId());
        existing.setCar(findCar(order.getCar()));
        existing.setPriority(order.getPriority());
        return orderRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order with id " + id + " does not exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Order order = findById(id);
        order.getWorks().forEach(work -> work.setOrder(null));
        workRepository.saveAll(order.getWorks());
        orderRepository.delete(order);
    }

    private void validate(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must be specified");
        }
        if (order.getCar() == null || order.getCar().getId() == null) {
            throw new IllegalArgumentException("A persisted car must be specified for the order");
        }
        if (order.getPriority() == null) {
            throw new IllegalArgumentException("Order priority must be specified");
        }
    }

    private Car findCar(Car car) {
        if (car == null || car.getId() == null) {
            throw new IllegalArgumentException("A persisted car must be specified");
        }
        return carRepository.findById(car.getId())
                .orElseThrow(() -> new IllegalArgumentException("Car with id " + car.getId() + " does not exist"));
    }

}
