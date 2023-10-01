package com.springboot.blog.controller;

import com.springboot.blog.payload.dto.CategoryDto;
import com.springboot.blog.payload.dto.LoginDto;
import com.springboot.blog.service.CategoryService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
//below annotation is for swagger-ui
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Category Api's")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Get Category by ID",
            description = "This API is used to get a category by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @PathVariable("id") Long categoryId
    ){
        return new ResponseEntity<>(this.categoryService.getCategoryById(categoryId), HttpStatus.OK);
    }

    @Operation(
            summary = "Get all categories",
            description = "This API is used to get all categories"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))
    )
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return new ResponseEntity<>(this.categoryService.getAllCategories(), HttpStatus.OK);
    }
}
