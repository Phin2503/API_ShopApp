package com.example.ShopApp.controllers;


import com.example.ShopApp.dtos.OrderDetailDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.models.OrderDetail;
import com.example.ShopApp.repositories.OrderDetailRepository;
import com.example.ShopApp.responses.OrderDetailResponse;
import com.example.ShopApp.responses.OrderResponse;
import com.example.ShopApp.services.ICategoryService;
import com.example.ShopApp.services.IOrderDetailService;
import com.example.ShopApp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${api.prefix}/order_details")
@AllArgsConstructor
public class OrderDetailController {

    private final IOrderDetailService orderDetailService;
    @PostMapping
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.
                        getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.formatOrderDetail(newOrderDetail));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) throws DataNotFoundException {
        OrderDetail orderDetail =  orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok().body(OrderDetailResponse.formatOrderDetail(orderDetail));
//        return ResponseEntity.ok(orderDetail);
    }


    //Lấy ra danh sách các order detail của 1 đơn hàng nào đó
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
       List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
       List<OrderDetailResponse> orderDetailResponses = orderDetails.stream().map(OrderDetailResponse::formatOrderDetail).toList();
        return ResponseEntity.ok(orderDetailResponses);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id, @RequestBody OrderDetailDTO newOrderDetailData) throws DataNotFoundException {
        try{
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id,newOrderDetailData);
            return ResponseEntity.ok().body(orderDetail);
        }catch (DataNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") long id) throws DataNotFoundException {
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok().body("Delete Order detail successfully with id = " + id );
        }catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
 }
