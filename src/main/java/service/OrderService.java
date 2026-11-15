package service;

import domain.Car;
import domain.Order;
import domain.Priority;
import dto.OrderDTO;

import java.util.List;

public interface OrderService {
    Order create(OrderDTO orderDTO);
    Order findById(Long id);
    List<Order> findAll();
    Order update(OrderDTO orderDTO);
    void delete(Long id);
}
