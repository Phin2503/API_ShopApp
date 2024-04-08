package com.example.ShopApp.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDTO {

    @JsonProperty(value = "user_id")
    @Min(value = 1, message = "User ID must be > 0")
    private Long userId;

    @JsonProperty(value = "full_name")
    private String fullName;

    @NotNull(message = "email is required")
    @Email(message = "Email is not valid")
    private String email;

    @JsonProperty(value = "phone_number")
    @Size(min = 10, message = "Phone Number must be t least 5 characters    ")
    private String phoneNumber;

    @NotNull(message = "address is required")
    private String address;

    private String note;

    @JsonProperty(value = "total_money")
    @Min(value = 0,message = "Total money must be >= 0")
    private Float totalMoney;

    @JsonProperty(value = "shipping_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate shippingDate;

    @JsonProperty(value = "shipping_method")
    private String shippingMethod;

    @JsonProperty(value = "payment_method")
    private String paymentMethod;

    @JsonProperty(value = "shipping_address")
    private String shippingAddress;

    @JsonProperty(value = "tracking_number")
    private String trackingNumber;

    @JsonProperty(value = "active")
    private Boolean active;
}
