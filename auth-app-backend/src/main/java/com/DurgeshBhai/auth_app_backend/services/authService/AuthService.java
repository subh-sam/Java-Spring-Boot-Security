package com.DurgeshBhai.auth_app_backend.services;

import com.DurgeshBhai.auth_app_backend.dtos.UserDto;

public interface AuthService {
    UserDto registerUser (UserDto userDto);
}
