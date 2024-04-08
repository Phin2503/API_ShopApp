package com.example.ShopApp.repositories;

import com.example.ShopApp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
}
