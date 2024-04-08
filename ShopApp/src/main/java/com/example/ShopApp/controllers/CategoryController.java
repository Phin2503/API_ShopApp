package com.example.ShopApp.controllers;

import com.example.ShopApp.dtos.CategoryDTO;
import com.example.ShopApp.models.Category;
import com.example.ShopApp.services.CategoryService;
import com.example.ShopApp.services.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//Dependency Injection
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> createCategories(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        String categoryName = categoryDTO.getName();
        Category categoryExists = categoryService.getCategoryByName(categoryName);

        if (categoryExists != null) {
            return ResponseEntity.badRequest().body("Category name already exists");
        } else {
            categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok("Inserted category successfully: " + categoryName);
        }
    }


    @GetMapping("")
    public ResponseEntity<List> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategories(@PathVariable long  id, @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("This is update successfully " + id + categoryDTO.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
        categoryService.DeleteCategory(id);
        return ResponseEntity.ok("Delete Category id = " + id + " successfully");
    }
}
