package com.springboot.blog.controller.admin;

import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.dto.CategoryDto;
import com.springboot.blog.payload.dto.UserDto;
import com.springboot.blog.service.AuthService;
import com.springboot.blog.service.UserService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
//below annotation is for swagger-ui
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Admin User Api's")
public class AdminUserController {
    private UserService userService;
    private AuthService authService;

    public AdminUserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Operation(
            summary = "Get all users",
            description = "This API is used to get all users and can be accessed only by Admin"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @GetMapping
    public ResponseEntity<PageResponse<UserDto>> getAllUsers(
            Authentication authentication,
            @RequestParam(value = "pageNum", defaultValue = AppConstants.DEFAULT_PAGE_NUM_VALUE, required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE_VALUE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.ASCENDING, required = false) String sortDir
    ){
        return new ResponseEntity<>(this.userService.getAllUsers(authentication, pageNum, pageSize, sortBy, sortDir), HttpStatus.OK);
    }


    @Operation(
            summary = "Create Admin user",
            description = "This API is used to create Admin user and can be accessed only by existing Admin"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            Authentication authentication,
            @RequestBody @Valid UserDto userDto
    ){
        return new ResponseEntity<>(this.authService.createUser(userDto, authentication), HttpStatus.CREATED);
    }

}
