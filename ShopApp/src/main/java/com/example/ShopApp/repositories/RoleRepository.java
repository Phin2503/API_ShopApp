package com.example.ShopApp.repositories;

import com.example.ShopApp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



public interface RoleRepository extends JpaRepository<Role, Long> {

}
