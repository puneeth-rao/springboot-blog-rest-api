package com.springboot.blog.service;

import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.dto.PostDto;
import com.springboot.blog.payload.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserDto getUserById(Long userId);
    UserDto updateUser(UserDto userDto, String userEmail);
    void deleteUser(String userEmail);
    PageResponse<UserDto> getAllUsers(Authentication authentication, Integer pageNum, Integer pageSize, String sortBy, String sortDir);
    PageResponse<UserDto> searchUsers(String keyword, Integer pageNumber, Integer pageSize);
}
