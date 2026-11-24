package service;

import domain.Order;

import java.util.List;

public interface OrderService {
    Order create(Order order);
    Order findById(Long id);
    List<Order> findAll();
    Order update(Order order);
    void delete(Long id);
}
