package com.springboot.blog.controller;

import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.dto.CommentDto;
import com.springboot.blog.payload.dto.UserDto;
import com.springboot.blog.repository.UserRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
//below annotation is for swagger-ui
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Api's")
public class UserController {
    private UserService userService;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get a user by userId",
            description = "This API is used to get a user by userId"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(this.userService.getUserById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Update a user",
            description = "This API is used to update the current user who is loggedIn"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, Authentication authentication){
        UserDto updatedUser = this.userService.updateUser(userDto, authentication.getName());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(
            summary = "delete a user",
            description = "This API is used to delete the current user who is loggedIn"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK",
            content = @Content(schema = @Schema(implementation = String.class))
    )
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteUserById(Authentication authentication){
        this.userService.deleteUser(authentication.getName());
        return new ResponseEntity<>(Map.of("message", "user deleted successfully"),HttpStatus.OK);
    }

    @Operation(
            summary = "search for users by full name",
            description = "This API is used to search for all users by their first name"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping("/search")
    public ResponseEntity<PageResponse<UserDto>> searchUsersByKeyword(
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = AppConstants.DEFAULT_PAGE_NUM_VALUE, required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE_VALUE, required = false) Integer pageSize
    ){
        return new ResponseEntity<>(this.userService.searchUsers(keyword, pageNum, pageSize),HttpStatus.OK);
    }

}
