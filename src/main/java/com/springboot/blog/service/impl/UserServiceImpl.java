package com.springboot.blog.service.impl;

import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.dto.UserDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.UserService;
import com.springboot.blog.utils.AppConstants;
import com.springboot.blog.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private AppUtils appUtils;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, AppUtils appUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.appUtils = appUtils;
    }


    @Override
    public UserDto getUserById(Long userId) {
        User foundUser = this.userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        return this.modelMapper.map(foundUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userEmail) {
        User user = this.userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new ResourceNotFoundException("User","email",userEmail));

        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        User updatedUser = this.userRepository.save(user);
        return this.modelMapper.map(updatedUser, UserDto.class);
    }

    //unable to run this request - check for foreign key constraints
    @Override
    public void deleteUser(String userEmail) {
        User userToBeDeleted = this.userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new ResourceNotFoundException("User","email",userEmail));
        this.userRepository.delete(userToBeDeleted);
    }

    @Override
    public PageResponse<UserDto> getAllUsers(Authentication authentication, Integer pageNum, Integer pageSize, String sortBy, String sortDir) {
        if(! appUtils.isAdmin(authentication))
            throw new BlogApiException("User not an Admin, Access Denied!!", HttpStatus.FORBIDDEN);

        Sort sort = sortDir.equalsIgnoreCase(AppConstants.ASCENDING) ?
                Sort.by(sortBy).ascending()  :
                Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNum, pageSize, sort);
        Page<User> pagedPosts = this.userRepository.findAll(page);
        List<User> users = pagedPosts.getContent();

        List<UserDto> userDtos = users.stream().map(user -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());

        PageResponse<UserDto> pagedPostResponse = new PageResponse<UserDto>(
                userDtos,
                pagedPosts.getNumber(),
                pagedPosts.getSize(),
                pagedPosts.getTotalElements(),
                pagedPosts.getTotalPages(),
                pagedPosts.isLast()
        );

        return pagedPostResponse;
    }

    @Override
    public PageResponse<UserDto> searchUsers(String keyword, Integer pageNumber, Integer pageSize) {
        Pageable p = PageRequest.of(pageNumber, pageSize);
        Page<User> pagedUsers = this.userRepository.findByFullNameContaining(keyword, p);
        List<User> users = pagedUsers.getContent();

        List<UserDto> userDtos = users.stream()
                .map(user -> this.modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        PageResponse<UserDto> userResponse = new PageResponse<>(
                userDtos,
                pagedUsers.getNumber(),
                pagedUsers.getSize(),
                pagedUsers.getTotalElements(),
                pagedUsers.getTotalPages(),
                pagedUsers.isLast()
        );
        return userResponse;
    }
}
