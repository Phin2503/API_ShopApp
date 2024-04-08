package com.example.ShopApp.repositories;

import com.example.ShopApp.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



public interface UserRepository  extends JpaRepository<Users, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Users> findByPhoneNumber(String phoneNumber);
}
