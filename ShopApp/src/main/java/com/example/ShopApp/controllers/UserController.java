package com.example.ShopApp.controllers;


import com.example.ShopApp.dtos.UserDTO;
import com.example.ShopApp.dtos.UserLoginDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.services.ICategoryService;
import com.example.ShopApp.services.IUserService;
import com.example.ShopApp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    @PostMapping(value = "/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }

            if (userDTO.getPassword() == null || !userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Password retype must be same password !");
            }else{
                userService.createUser(userDTO);
                return ResponseEntity.ok("Register successfully !");
            }

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
