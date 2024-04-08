package com.example.ShopApp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty(value = "full_name")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @JsonProperty(value = "phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Password can't be blank")
    private String password;

    @JsonProperty(value = "retype_password")
    private String retypePassword;

    @JsonProperty(value = "date_of_birth")
    private Date dateOfBirth;

    @JsonProperty(value = "facebook_account_id")
    private int FacebookAcoountId;

    @JsonProperty(value = "google_account_id")
    private int googleAccountId;

    @NotNull(message = "Role ID is required")
    @JsonProperty(value = "role_id")
    private Long roleId;

    @JsonProperty(value = "is_active")
    private Boolean active;

}
