package com.example.ShopApp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BaseResponse {
    @JsonProperty("created_at")
    private LocalDate createAt;

    @JsonProperty("updated_at")
    private LocalDate updateAt;
}
