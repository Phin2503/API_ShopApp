package com.example.ShopApp.controllers;

import com.example.ShopApp.dtos.ProductDTO;
import com.example.ShopApp.dtos.ProductImageDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.models.Product;
import com.example.ShopApp.models.ProductImage;
import com.example.ShopApp.responses.ProductListResponse;
import com.example.ShopApp.responses.ProductResponse;
import com.example.ShopApp.services.IProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductsController {

    private final IProductService productService;
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(@RequestParam("page") int page,
                                                           @RequestParam("limit") int limit){
        PageRequest pageRequest = PageRequest.of(page,limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productsPage = productService.getAllProducts(pageRequest);
        // Tong so trang
        int totalPages = productsPage.getTotalPages();
        List<ProductResponse> products =  productsPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductByID(@PathVariable("id") int id) throws Exception {
        try {
            Product product =   productService.getProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Cound not find product with id = " + id);
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductByID(@PathVariable("id") long id){
       try{
           productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("Products with id = %d deleted successfully", id));
       }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @PostMapping( value = "")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                           BindingResult result){
        try {
                if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }
           Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.OK).body(newProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping( value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //POST http://localhost:8080/v1/api/products
    public ResponseEntity<?> uploadImages(@PathVariable("id") long productId,@ModelAttribute("files") List<MultipartFile> files) throws Exception {
        try {

            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > 5) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images ");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for(MultipartFile file : files) {
                if(file.getSize() == 0) {
                    continue;
                }
                if(file != null) {
                    //Kiểm tra kích thước file
                    if(file.getSize() > 10 * 1024 * 1024){// Kích thước lớn hơn 10MB
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large ! Maximum size is 10MB");
                    }
                    // Kiểm tra định dạng fle
                    String contentType = file.getContentType();
                    if(contentType == null || !contentType.startsWith("image/")){
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                    }
                    String fileName = storeFile(file);

                    ProductImage productImage = productService.createProductImage(existingProduct.getId(), ProductImageDTO.builder()
                            .imageUrl(fileName)
                            .build());

                    productImages.add(productImage);
                }

            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }


    private String storeFile(MultipartFile file) throws IOException {

            if(!IsImageFile(file) || file.getOriginalFilename() != null){
                throw new IOException("Ivalid image format ");
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            // Thêm UUID trước tên file upload để đảm bảo không bị trùng
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            // Đường dẫn thư mục muốn lưu file
            java.nio.file.Path uploadDir = Paths.get("uploads");
            // Kiểm tra và tạo thư mục nếu nó không tồn tại
            if(!Files.exists(uploadDir)){
                Files.createDirectories(uploadDir);
            }
            // Đường dẫn đầy đủ đến file
           java.nio.file.Path destination = Paths.get(uploadDir.toString(),uniqueFileName);

            Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
    }

    private boolean IsImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i = 0; i < 1000; i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10, 90000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(1,3))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products successfully");
    }
}
