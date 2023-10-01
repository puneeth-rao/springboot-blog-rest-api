package com.springboot.blog.controller.admin;

import com.springboot.blog.payload.dto.CategoryDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
//below annotation is for swagger-ui
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Admin Category Api's")
public class AdminCategoryController {

    private CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Create category",
            description = "This API is used to create unique category and can be accessed only by Admin"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))
    )
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody @Valid CategoryDto categoryDto
    ){
        return new ResponseEntity<>(
                this.categoryService.createCategory(categoryDto),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Update category",
            description = "This API is used to update existing category and can be accessed only by Admin"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable("id") Long categoryId,
            @RequestBody @Valid CategoryDto categoryDto
    ){
        return new ResponseEntity<>(this.categoryService.updateCategory(categoryId, categoryDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete category",
            description = "This API is used to delete existing category and can be accessed only by Admin"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable("id") Long categoryId
    ){
        this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category deleted successfully!!", HttpStatus.OK);
    }

}
