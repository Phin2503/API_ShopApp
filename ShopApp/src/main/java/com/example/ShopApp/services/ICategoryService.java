package com.example.ShopApp.services;

import com.example.ShopApp.dtos.CategoryDTO;
import com.example.ShopApp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryByName(String name);
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long id, CategoryDTO category);
    void DeleteCategory(long id);
}
