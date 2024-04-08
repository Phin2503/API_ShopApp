package com.example.ShopApp.controllers;

import com.example.ShopApp.dtos.OrderDTO;
import com.example.ShopApp.models.Order;
import com.example.ShopApp.responses.OrderResponse;
import com.example.ShopApp.services.IOrderService;
import com.example.ShopApp.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result){
        try{
            if(result.hasErrors()){
               List<String> errorMessage =  result.getFieldErrors()
                                            .stream()
                                            .map(FieldError::getDefaultMessage)
                                            .toList();

               return ResponseEntity.badRequest().body(errorMessage);
            }

            Order orderResponse =  orderService.createOrder(orderDTO);
            return ResponseEntity.ok("create order successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    //GET http://localhost:8080/api/v1/orders/user/4
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
        try{
            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId){
        try{
            Optional<Order> existingOrder =  orderService.getOrder(orderId);
            return ResponseEntity.ok(existingOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable("id") Long id, @Valid @RequestBody OrderDTO orderDTO){
        try {
            Optional<Order> order = Optional.ofNullable(orderService.updateOrder(id, orderDTO));
            return ResponseEntity.ok(order);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("id") Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Delete orders successfully");
    }
}
