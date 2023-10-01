package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.JwtAuthResponse;
import com.springboot.blog.payload.dto.LoginDto;
import com.springboot.blog.payload.dto.UserDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtHelper;
import com.springboot.blog.service.AuthService;
import com.springboot.blog.utils.AppConstants;
import com.springboot.blog.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    //this how spring handles actual authentication
    //here we are manually doing authentication process without taking the help of spring's auto authorization

    private AuthenticationManager authenticationManager;
    private JwtHelper jwtHelper;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private AppUtils appUtils;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtHelper jwtHelper, UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, AppUtils appUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.appUtils = appUtils;
    }

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {
        //as we have email and password we don't need to filter that extracts the same
        //.authenticate()
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                ));

        //storing authentication inside securityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(
                jwtHelper.generateToken(authentication),            //token
                AppConstants.BEARER
        );

        return jwtAuthResponse;
    }

    //default create user by normal user , role is always ROLE_USER
    @Override
    public UserDto createUser(UserDto userDto, Authentication authentication) {
        if(this.userRepository.existsByEmail(userDto.getEmail())) {
            throw new BlogApiException("User Email already registered!! Either login or create account using new email ID..", HttpStatus.BAD_REQUEST);
        }

        Set<Role> roles = new HashSet<>();
        Role role;
        if(authentication != null && appUtils.isAdmin(authentication)){
             role = this.roleRepository.findByName(AppConstants.ROLE_ADMIN)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Role", "name", AppConstants.ROLE_ADMIN
                    ));
        } else {
            role = this.roleRepository.findByName(AppConstants.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Role", "name", AppConstants.ROLE_USER
                    ));
        }

        roles.add(role);

        User user = this.modelMapper.map(userDto, User.class);
        user.setRoles(roles);
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        User savedUser = this.userRepository.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }
}
