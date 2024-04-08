package com.example.ShopApp.repositories;

import com.example.ShopApp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);

    default Page<Product> findAll(Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return findAll(pageable);
    }
}
