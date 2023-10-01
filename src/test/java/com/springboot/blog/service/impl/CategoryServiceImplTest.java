package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.dto.CategoryDto;
import com.springboot.blog.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock private CategoryRepository categoryRepository;
    @Mock private ModelMapper modelMapper;
    @InjectMocks private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreateCategory_success() {
        Category category = new Category();
        category.setName("test");
        category.setId(1L);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("test");
        categoryDto.setId(1L);

        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(modelMapper.map(any(CategoryDto.class), eq(Category.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(modelMapper.map(any(Category.class), eq(CategoryDto.class))).thenReturn(categoryDto);

        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        assertNotNull(createdCategory);
        assertEquals(category.getName().toUpperCase(), createdCategory.getName());
    }

    @Test
    void testCreateCategory_exception() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("test");
        categoryDto.setId(1L);

        when(categoryRepository.existsByName(anyString())).thenReturn(true);
        assertThrows(BlogApiException.class, ()->{
            categoryService.createCategory(categoryDto);
        });
    }

    @Test
    void testGetCategoryById_success() {
        Category category = new Category();
        category.setName("test");
        category.setId(1L);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("test");
        categoryDto.setId(1L);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(modelMapper.map(any(Category.class), eq(CategoryDto.class))).thenReturn(categoryDto);
        CategoryDto categoryDtoResult = categoryService.getCategoryById(1l);

        assertNotNull(categoryDtoResult);
        assertEquals(category.getName(), categoryDtoResult.getName());
    }

    @Test
    void testGetCategoryById_exception() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, ()->{
            categoryService.getCategoryById(1L);
        });
    }

    @Test
    void testGetAllCategories() {
        Category category1 = new Category();
        category1.setName("test1");
        category1.setId(1L);

        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setName("test1");
        categoryDto1.setId(1L);

        Category category2 = new Category();
        category2.setName("test2");
        category2.setId(2L);

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setName("test2");
        categoryDto2.setId(2L);

        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);
        when(modelMapper.map(category1, CategoryDto.class)).thenReturn(categoryDto1);
        when(modelMapper.map(category2, CategoryDto.class)).thenReturn(categoryDto2);

        List<CategoryDto> categoryDtos = this.categoryService.getAllCategories();

        assertEquals(category1.getName(), categoryDtos.get(0).getName());
        assertEquals(category2.getName(), categoryDtos.get(1).getName());
    }

    @Test
    void testUpdateCategory() {
        Category category = new Category();
        category.setName("test");
        category.setId(1L);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("test modified");
        categoryDto.setId(1L);

        when(this.categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(this.categoryRepository.existsByName(anyString())).thenReturn(false);
        category.setName("test modified");
        when(this.categoryRepository.save(any(Category.class))).thenReturn(category);
        when(this.modelMapper.map(any(), eq(CategoryDto.class))).thenReturn(categoryDto);

        CategoryDto updatedCategory = this.categoryService.updateCategory(1L, categoryDto);

        assertNotNull(updatedCategory);
        assertEquals(category.getName().toUpperCase(), updatedCategory.getName());
    }

    @Test
    public void testUpdateCategory_ResourceNotFoundException(){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("test modified");
        categoryDto.setId(1L);

        when(this.categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()->{
            categoryService.updateCategory(1L, categoryDto);
        });
    }

    @Test
    public void testUpdateCategory_DuplicateNameException(){
        Category category = new Category();
        category.setName("test");
        category.setId(1L);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("test modified");
        categoryDto.setId(1L);

        when(this.categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(this.categoryRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(BlogApiException.class, ()->{
            categoryService.updateCategory(1L, categoryDto);
        });
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category();
        category.setName("test");
        category.setId(1L);

        when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        this.categoryService.deleteCategory(category.getId());
        verify(this.categoryRepository, times(1)).delete(category);
    }

    @Test
    void testDeleteCategory_ResourceNotFoundException() {
        when(this.categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, ()->{
            this.categoryService.deleteCategory(1L);
        });
    }
}