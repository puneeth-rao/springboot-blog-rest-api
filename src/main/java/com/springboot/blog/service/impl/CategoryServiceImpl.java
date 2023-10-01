package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.dto.CategoryDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        categoryDto.setName(categoryDto.getName().toUpperCase());
        if(this.categoryRepository.existsByName(categoryDto.getName()))
            throw new BlogApiException("Category Name already exists!!", HttpStatus.BAD_REQUEST);
        Category category = this.modelMapper.map(categoryDto, Category.class);
        Category savedCategory = this.categoryRepository.save(category);
        return this.modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Category","id",categoryId)
                );
        return this.modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        return categories.stream()
                .map(category ->
                        this.modelMapper.map(category, CategoryDto.class)
                ).collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","id",categoryId));

        categoryDto.setName(categoryDto.getName().toUpperCase());
        if(this.categoryRepository.existsByName(categoryDto.getName()))
            throw new BlogApiException("Category Name already exists!!", HttpStatus.BAD_REQUEST);

        category.setName(categoryDto.getName());
        Category updatedCategory = this.categoryRepository.save(category);

        return this.modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","id",categoryId));
        this.categoryRepository.delete(category);
    }
}
