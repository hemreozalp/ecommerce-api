package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.CategoryRequest;
import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCategory_shouldReturnCategoryResponse() throws Exception {
        CategoryRequest request = new CategoryRequest("Electronics");
        CategoryResponse response = new CategoryResponse(1L, "Electronics");

        given(categoryService.createCategory(any(CategoryRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void getAllCategories_shouldReturnListOfCategories() throws Exception {
        List<CategoryResponse> categories = List.of(
                new CategoryResponse(1L, "Electronics"),
                new CategoryResponse(2L, "Books")
        );

        given(categoryService.getAllCategories()).willReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void getCategoryById_shouldReturnCategory() throws Exception {
        CategoryResponse response = new CategoryResponse(1L, "Electronics");

        given(categoryService.getCategoryById(1L)).willReturn(response);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategory() throws Exception {
        CategoryRequest request = new CategoryRequest("Updated");
        CategoryResponse response = new CategoryResponse(1L, "Updated");

        given(categoryService.updateCategory(eq(1L), any(CategoryRequest.class))).willReturn(response);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void deleteCategory_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(1L);
    }
}
