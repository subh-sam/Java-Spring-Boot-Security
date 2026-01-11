package com.DurgeshBhai.auth_app_backend.services;

import com.DurgeshBhai.auth_app_backend.dtos.UserDto;
import com.DurgeshBhai.auth_app_backend.entity.Provider;
import com.DurgeshBhai.auth_app_backend.entity.User;
import com.DurgeshBhai.auth_app_backend.exceptions.ResourceNotFoundException;
import com.DurgeshBhai.auth_app_backend.helpers.UserHelper;
import com.DurgeshBhai.auth_app_backend.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public ModelMapper modelMapper;
    @Override
    public UserDto createUser(UserDto userDto) {

        if (userDto.getEmail()==null || userDto.getEmail().isBlank()){
            throw new IllegalArgumentException("Email is required");
        }
        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new IllegalArgumentException("Email Already Exists");
        }

        User user = modelMapper.map(userDto,User.class);
        user.setProvider(userDto.getProvider()!=null ? userDto.getProvider() : Provider.LOCAL);
        User savedUser = userRepository.save(user);
        //for role
        //TODO:
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User Not Found To the give mail id"));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uid= UserHelper.parseUUID(userId);
        User existingUser = userRepository
                .findById(uid)
                .orElseThrow(()->new ResourceNotFoundException("User Not Found To the given User Id"));
        if (userDto.getName()!=null) existingUser.setName(userDto.getName());
        if (userDto.getImage()!=null) existingUser.setImage(userDto.getImage());
        //TODO: change password
        if (userDto.getPassword()!=null) existingUser.setPassword(userDto.getPassword());
        if (userDto.getProvider()!=null) existingUser.setProvider(userDto.getProvider());
        existingUser.setEnable(userDto.isEnable());
        existingUser.setUpdatedAt(Instant.now());
        User user = userRepository.save(existingUser);
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uid= UserHelper.parseUUID(userId);
        User user = userRepository.findById(uid).orElseThrow(()->new ResourceNotFoundException("User Not Found To the given User Id"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        UUID uid= UserHelper.parseUUID(userId);
        User user = userRepository.findById(uid).orElseThrow(()->new ResourceNotFoundException("User Not Found To the given User Id"));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user,UserDto.class))
                .toList();
    }
}
