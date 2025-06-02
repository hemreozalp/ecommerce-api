package com.ecommerce.product_service.controller;


import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.service.CategoryService;
import com.ecommerce.product_service.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct_shouldReturnProductResponse() throws Exception {
        ProductRequest request = new ProductRequest(
                "Test",
                "Description",
                BigDecimal.valueOf(99.99),
                10,
                1L
        );

        ProductResponse response = new ProductResponse(
                1L,
                "Test",
                "Description",
                BigDecimal.valueOf(99.99),
                10,
                new CategoryResponse(1L, "Category 1")
        );

        when(productService.createProduct(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    void getProductById_shouldReturnProduct() throws Exception {
        ProductResponse response = new ProductResponse(
                1L,
                "Test Product",
                "Test Description",
                BigDecimal.valueOf(100.00),
                10,
                new CategoryResponse(1L, "Category 1"));

        given(productService.getProductById(1L)).willReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(100.00));
    }

    @Test
    void getProductsByCategoryId_shouldReturnPageOfProducts() throws Exception {
        ProductResponse product = new ProductResponse(
                1L,
                "Test Product",
                "Desc",
                BigDecimal.valueOf(50.00),
                5,  new CategoryResponse(2L, "Category 2"));
        Page<ProductResponse> page = new PageImpl<>(List.of(product));

        given(productService.getProductsByCategoryId(eq(2L), any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/api/products/category/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].category.id").value(2L));
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {
        ProductRequest request = new ProductRequest(
                "Updated Product",
                "Updated Desc",
                BigDecimal.valueOf(150.00),
                7,
                3L);
        ProductResponse response = new ProductResponse(
                1L,
                "Updated Product",
                "Updated Desc",
                BigDecimal.valueOf(150.00),
                7,
                new CategoryResponse(3L, "Category 3"));

        given(productService.updateProduct(eq(1L), any(ProductRequest.class))).willReturn(response);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(150.00));
    }

    @Test
    void deleteProduct_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }
}
