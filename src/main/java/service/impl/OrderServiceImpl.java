package service.impl;

import domain.Order;
import dto.OrderDTO;
import service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    @Override
    public Order create(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public Order findById(Long id) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }

    @Override
    public Order update(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
