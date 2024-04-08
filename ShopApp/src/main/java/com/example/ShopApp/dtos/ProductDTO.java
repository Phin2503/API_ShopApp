package com.example.ShopApp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ProductDTO {
    @NotBlank(message = "Tilte is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 1000000, message = "Price must be less than or equal to 10,000,000")
    private Float price;
    private String thumbnail;

    private String description;

    @NotNull(message = "Category ID is required")
    @JsonProperty("category_id")
    private long categoryId;


}
