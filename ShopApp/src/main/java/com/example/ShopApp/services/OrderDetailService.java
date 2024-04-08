package com.example.ShopApp.services;

import com.example.ShopApp.dtos.OrderDetailDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.models.Order;
import com.example.ShopApp.models.OrderDetail;
import com.example.ShopApp.models.Product;
import com.example.ShopApp.repositories.OrderDetailRepository;
import com.example.ShopApp.repositories.OrderRepository;
import com.example.ShopApp.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailService implements IOrderDetailService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        // Check orderID tồn tại k
        System.out.println(orderDetailDTO.getOrderId());
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).
                orElseThrow(() -> new DataNotFoundException("Cannot find order with id =  " + orderDetailDTO.getOrderId()));
        // Check productID tồn tại k
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id = " + orderDetailDTO.getProductId()));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProduct())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find orderDetail with id " + id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException {
            // tim xem order detail co ton tai k
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id = " + id));
        Order existingOrder = orderRepository.findById(newOrderDetailData.getOrderId()).orElseThrow(() -> new DataNotFoundException("Cannot find order with id == " + newOrderDetailData.getOrderId()));
        System.out.println(newOrderDetailData.getOrderId());
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find product with id = " + newOrderDetailData.getProductId()));

        existingOrderDetail.setPrice(newOrderDetailData.getPrice());
        existingOrderDetail.setTotalMoney(newOrderDetailData.getTotalMoney());
        existingOrderDetail.setColor(newOrderDetailData.getColor());
        existingOrderDetail.setNumberOfProducts(newOrderDetailData.getNumberOfProduct());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);

        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) throws DataNotFoundException {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id =     " + id));
        if(existingOrderDetail != null) {
            orderDetailRepository.deleteById(id);
        }
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
