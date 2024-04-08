package com.example.ShopApp.services;

import com.example.ShopApp.dtos.OrderDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.models.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws DataNotFoundException;

    Optional<Order> getOrder(Long id);

    Order updateOrder(Long id , OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(Long id);

    List<Order> findByUserId(long userId);
}
