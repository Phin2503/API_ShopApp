package com.example.ShopApp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class OrderResponse extends BaseResponse {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;
    private String note;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    private String status;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty(value = "shipping_date")
    private Date shippingDate;

    @JsonProperty(value = "shipping_method")
    private String shippingMethod;

    @JsonProperty(value = "payment_method")
    private String paymentMethod;

    @JsonProperty(value = "shipping_address")
    private String shippingAddress;

    @JsonProperty(value = "tracking_number")
    private String trackingNumber;

    @JsonProperty(value = "active")
    private boolean active;
}
