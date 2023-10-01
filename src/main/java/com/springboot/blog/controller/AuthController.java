package com.springboot.blog.controller;

import com.springboot.blog.payload.JwtAuthResponse;
import com.springboot.blog.payload.dto.CategoryDto;
import com.springboot.blog.payload.dto.LoginDto;
import com.springboot.blog.payload.dto.UserDto;
import com.springboot.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Login/signup Api's")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //http://localhost:8080/auth/login or http://localhost:8080/auth/signin
    @Operation(
            summary = "login API",
            description = "This API is used to login with existing account using email and password and JWT token is returned upon successful authentication"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK",
            content = @Content(schema = @Schema(implementation = LoginDto.class))
    )
    @PostMapping({"/login", "/signin"})
    public ResponseEntity<JwtAuthResponse> login(
            @RequestBody @Valid LoginDto loginDto
            ){
        return new ResponseEntity<>(this.authService.login(loginDto), HttpStatus.OK);
    }

    @Operation(
            summary = "signup/register API",
            description = "This API is used to register user with USER-role"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @PostMapping({"/signup", "/register"})
    public ResponseEntity<UserDto> createUser(
            @RequestBody @Valid UserDto userDto
    ){
        return new ResponseEntity<>(this.authService.createUser(userDto, null), HttpStatus.CREATED);
    }
}
