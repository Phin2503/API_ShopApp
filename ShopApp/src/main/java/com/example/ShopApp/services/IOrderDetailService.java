package com.example.ShopApp.services;

import com.example.ShopApp.dtos.OrderDTO;
import com.example.ShopApp.dtos.OrderDetailDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException;
    void deleteOrderDetail(Long id) throws DataNotFoundException;
    List<OrderDetail> findByOrderId(Long orderId);
}
