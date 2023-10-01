package com.springboot.blog.service;

import com.springboot.blog.payload.dto.UserDto;

import java.util.List;

public interface AdminService {
    UserDto createUser(UserDto user);  //role of admin
    UserDto updateUser(UserDto user, Long userId);
    UserDto getUserById(Long userId);
    List<UserDto> getAllUsers();
    void deleteUser(Long userId);
}
