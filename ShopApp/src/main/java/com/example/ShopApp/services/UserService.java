package com.example.ShopApp.services;

import com.example.ShopApp.components.JwtTokenUtil;
import com.example.ShopApp.dtos.UserDTO;
import com.example.ShopApp.exception.DataNotFoundException;
import com.example.ShopApp.exception.InvalidParamsException;
import com.example.ShopApp.exception.PermissionDenyException;
import com.example.ShopApp.models.Role;
import com.example.ShopApp.models.Users;
import com.example.ShopApp.repositories.RoleRepository;
import com.example.ShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public Users createUser(UserDTO userDTO) throws Exception {
        //register
        String phoneNumber = userDTO.getPhoneNumber();
        // Kiem tra so dt ton tai chua
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number is ready exists");
        }
        //convert from userDTO => user
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Role not found"));
        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("You can't register a admin account");
        }
            Users newUser = Users.builder()
                    .fullName(userDTO.getFullName())
                    .phoneNumber(userDTO.getPhoneNumber())
                    .password(userDTO.getPassword())
                    .address(userDTO.getAddress())
                    .dateOfBirth(userDTO.getDateOfBirth())
                    .googleAccountId(userDTO.getGoogleAccountId())
                    .FacebookAccountId(userDTO.getFacebookAcoountId())
                    .active(userDTO.getActive())
                    .build();

            newUser.setRole(role);
            //kiem tra xem co accountID , khong yeu cau password
            if(userDTO.getGoogleAccountId() == 0 && userDTO.getFacebookAcoountId() == 0) {
                String password =  userDTO.getPassword();
                String encodedPassword =  passwordEncoder.encode(password);
                newUser.setPassword(encodedPassword);
            }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamsException {
        Optional<Users> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid phonenumber / password");
        }
        Users existingUser = optionalUser.get();
        // Check password
        if(existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0){
            if(!passwordEncoder.matches(password,existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password
        );
//        return optionalUser.get();// Phải trả về token
        //authenticate with Java spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);

    }

}
