package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.CategoryRequest;
import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.model.Category;
import com.ecommerce.product_service.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createCategory_shouldReturnCategoryResponse(){
        CategoryRequest request = new CategoryRequest("Electronics");
        Category category = new Category(1L, "Electronics");

        when(categoryRepository.existsByName("Electronics")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse response = categoryService.createCategory(request);

        assertNotNull(response);
        assertEquals("Electronics", response.getName());
        assertEquals(1L, response.getId());
    }

    @Test
    void getAllCategories_shouldReturnListOfResponses() {
        List<Category> categories = Arrays.asList(
                new Category(1L, "Electronics"),
                new Category(2L, "Books")
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryResponse> responses = categoryService.getAllCategories();

        assertEquals(2, responses.size());
        assertEquals("Electronics", responses.get(0).getName());
    }

    @Test
    void getCategoryById_shouldReturnCategoryResponse() {
        Category category = new Category(1L, "Electronics");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponse response = categoryService.getCategoryById(1L);

        assertEquals("Electronics", response.getName());
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategoryResponse() {
        Category existing = new Category(1L, "Electronics");
        CategoryRequest request = new CategoryRequest("NewName");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResponse response = categoryService.updateCategory(1L, request);

        assertEquals("NewName", response.getName());
    }

    @Test
    void deleteCategory_shouldCallRepositoryDelete() {
        Category category = new Category(1L, "Electronics");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(category);
    }
}

