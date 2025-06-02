package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.model.Category;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Electronics");
        product = new Product(1L, "Phone", "Smartphone", new BigDecimal("1000"), 10, category);
    }

    @Test
    void createProduct_shouldReturnProductResponse() {
        ProductRequest request = new ProductRequest("Phone", "Smartphone", new BigDecimal("1000"), 10, 1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals("Phone", response.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getAllProducts_shouldReturnPageOfProductResponse() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductResponse> responsePage = productService.getAllProducts(pageable);

        assertEquals(1, responsePage.getTotalElements());
        assertEquals("Phone", responsePage.getContent().get(0).getName());
    }

    @Test
    void getProductById_existingId_shouldReturnProductResponse() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals("Phone", response.getName());
    }

    @Test
    void getProductById_nonExistingId_shouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.getProductById(99L));
        assertEquals("Product not found: 99", ex.getMessage());
    }

    @Test
    void updateProduct_existingId_shouldReturnUpdatedProductResponse() {
        ProductRequest updateRequest = new ProductRequest("NewPhone", "Updated", new BigDecimal("900"), 10, 1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.updateProduct(1L, updateRequest);

        assertEquals("NewPhone", response.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_existingId_shouldDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }
}
