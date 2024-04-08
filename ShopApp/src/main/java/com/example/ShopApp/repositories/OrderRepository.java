package com.example.ShopApp.repositories;

import com.example.ShopApp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long userId);
}
