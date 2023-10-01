package com.springboot.blog.service;

import com.springboot.blog.payload.JwtAuthResponse;
import com.springboot.blog.payload.dto.LoginDto;
import com.springboot.blog.payload.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface AuthService {
    JwtAuthResponse login(LoginDto loginDto);

    //default create user by normal user , role is always ROLE_USER
    UserDto createUser(UserDto userDto, Authentication authentication);
}
