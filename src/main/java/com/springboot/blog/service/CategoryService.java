package com.springboot.blog.service;

import com.springboot.blog.payload.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto getCategoryById(Long categoryId);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);
    void deleteCategory(Long categoryId);
}
