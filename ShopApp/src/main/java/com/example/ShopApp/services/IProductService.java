package com.example.ShopApp.services;


import com.example.ShopApp.dtos.ProductDTO;
import com.example.ShopApp.dtos.ProductImageDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.exception.InvalidParamsException;
import com.example.ShopApp.models.Product;
import com.example.ShopApp.models.ProductImage;
import com.example.ShopApp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct (ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById (long id) throws DataNotFoundException, Exception;

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Long id) throws DataNotFoundException;

    boolean existsByName(String name);
     ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamsException;


}
