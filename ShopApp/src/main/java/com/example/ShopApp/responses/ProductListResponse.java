package com.example.ShopApp.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
