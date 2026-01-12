package com.DurgeshBhai.auth_app_backend.services;

import com.DurgeshBhai.auth_app_backend.dtos.UserDto;
import com.DurgeshBhai.auth_app_backend.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserService userService;

    @Override
    public UserDto registerUser(UserDto userDto) {
        return userService.createUser(userDto);
    }
}
