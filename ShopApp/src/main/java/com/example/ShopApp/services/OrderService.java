package com.example.ShopApp.services;

import com.example.ShopApp.dtos.OrderDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.models.Order;
import com.example.ShopApp.models.OrderStatus;
import com.example.ShopApp.models.Users;
import com.example.ShopApp.repositories.OrderRepository;
import com.example.ShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class OrderService implements IOrderService{

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;
    @Override
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        Users user = userRepository.findById(orderDTO.getUserId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find user with id = " + orderDTO.getUserId()));

        //convert orderDTO sang cho thằng order
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        Order newOrder = new Order();
        modelMapper.map(orderDTO,newOrder);
        newOrder.setUser(user);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today!");
        }
        newOrder.setActive(true);
        newOrder.setShippingDate(shippingDate);
        orderRepository.save(newOrder);

        return newOrder;
    }

    @Override
    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find order with id = " + id));
        Users existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("Cannot find user id = " + orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));

        modelMapper.map(orderDTO,existingOrder);
        existingOrder.setUser(existingUser);

        return orderRepository.save(existingOrder);

    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        // no hare delete, => please soft-delete
        if(order != null){
            order.setActive(false);
            orderRepository.save(order);
        }
    }


    @Override
    public List<Order> findByUserId(long userId) {
        return orderRepository.findByUserId(userId);
    }
}
