package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.model.Category;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Category category = getCategoryById(request.getCategoryId());
        Product product = ProductMapper.toEntity(request, category);
        Product saved = productRepository.save(product);
        logger.info("Creating product: {}", request.getName());
        return ProductMapper.toResponse(saved);
    }

    @Cacheable(value = "allProducts", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductMapper::toResponse);
    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(Long id) {
        Product product = findProductById(id);
        logger.info("Fetching product with ID: {}", id);
        return ProductMapper.toResponse(product);
    }

    @Cacheable(value = "categoryProducts", key = "#categoryId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponse> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        Category category = getCategoryById(categoryId);
        return productRepository.findByCategory(category, pageable)
                .map(ProductMapper::toResponse);
    }

    @CacheEvict(value = "products", key = "#id")
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existing = findProductById(id);

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setStock(request.getStock());

        Category newCategory = getCategoryById(request.getCategoryId());
        existing.setCategory(newCategory);

        Product updated = productRepository.save(existing);
        logger.info("Updating product with ID: {}", id);
        return ProductMapper.toResponse(updated);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        Product existing = findProductById(id);
        productRepository.delete(existing);
        logger.warn("Deleting product with ID: {}", id);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", id);
                    return new RuntimeException("Product not found: " + id);
                });
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with ID: {}", id);
                    return new RuntimeException("Category not found: " + id);
                });
    }

}
