package com.example.ShopApp.services;

import com.example.ShopApp.dtos.UserDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.exception.InvalidParamsException;
import com.example.ShopApp.models.Users;
import org.apache.catalina.User;

public interface IUserService {
    Users createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber,String password) throws DataNotFoundException, InvalidParamsException;
}
