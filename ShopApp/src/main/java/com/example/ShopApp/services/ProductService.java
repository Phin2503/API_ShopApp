package com.example.ShopApp.services;

import com.example.ShopApp.dtos.ProductDTO;
import com.example.ShopApp.dtos.ProductImageDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.exception.InvalidParamsException;
import com.example.ShopApp.models.Category;
import com.example.ShopApp.models.Product;
import com.example.ShopApp.models.ProductImage;
import com.example.ShopApp.repositories.CategoryRepository;
import com.example.ShopApp.repositories.ProductImageRepository;
import com.example.ShopApp.repositories.ProductRepository;
import com.example.ShopApp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
      Category existsCategory = categoryRepository
              .findById(productDTO.getCategoryId())
                .orElseThrow(()
                        -> new DataNotFoundException("Cannot find category with id : " + productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .category(existsCategory)
                .thumbnail(productDTO.getThumbnail())
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long id) throws Exception {
        return productRepository.findById(id).orElseThrow(
                () ->new DataNotFoundException("Cannot find product with id : "  + id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(product -> {
           ProductResponse productResponse = ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .categoryId(product.getCategory().getId())
                    .description(product.getDescription())
                    .thumbnail(product.getThumbnail())
                    .price(product.getPrice())
                    .build();
           productResponse.setCreateAt(LocalDate.from(product.getCreatedAt()));
           productResponse.setUpdateAt(LocalDate.from(product.getUpdatedAt()));
        return productResponse;
        });
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existsProduct = getProductById(id);
        if(existsProduct != null) {
            //copy cac thuoc tinh tu DTO -> Product
            //Co the su dung modelMapper
            Category existsCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(()
                            -> new DataNotFoundException("Cannot find category with id : " + productDTO.getCategoryId()));

            existsProduct.setName(productDTO.getName());
            existsProduct.setCategory(existsCategory);
            existsProduct.setPrice(productDTO.getPrice());
            existsProduct.setThumbnail(productDTO.getThumbnail());
            existsProduct.setDescription(productDTO.getDescription());
            return productRepository.save(existsProduct);
        }else{
            return null;
        }

    }

    @Override
    public void deleteProduct(Long id) throws DataNotFoundException {
        Optional<Product> existsProduct = productRepository.findById(id);
        if (existsProduct.isPresent()) {
            productRepository.delete(existsProduct.get());
        } else {
            throw new DataNotFoundException("Product not found with id: " + id);
        }
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamsException {
        Product existsingProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id = " + productImageDTO.getProductId()));

        ProductImage newProductImage = ProductImage.builder()
                .product(existsingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        //Khong cho insert qua 5 anh cho 1 san pham

        int size = productImageRepository.findByProductId(productId).size();
        if(size >= 5) {
            throw new InvalidParamsException("Number of Image must be <= 5");
        }
        return productImageRepository.save(newProductImage);
    }
}
