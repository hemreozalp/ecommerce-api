package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.CategoryRequest;
import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.model.Category;
import com.ecommerce.product_service.mapper.CategoryMapper;
import com.ecommerce.product_service.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class); // sınıfın en üstüne

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            logger.warn("Category already exists with name: {}", request.getName());
            throw new IllegalArgumentException("Category already exists: " + request.getName());
        }
        Category category = CategoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);
        logger.info("Category created with ID: {}", saved.getId());
        return CategoryMapper.toResponse(saved);
    }


    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = findCategoryById(id);
        return CategoryMapper.toResponse(category);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category existing = findCategoryById(id);
        existing.setName(request.getName());
        Category updated = categoryRepository.save(existing);
        logger.info("Category updated with ID: {}", updated.getId());
        return CategoryMapper.toResponse(updated);
    }

    public void deleteCategory(Long id) {
        Category existing = findCategoryById(id);
        categoryRepository.delete(existing);
        logger.info("Category deleted with ID: {}", id);
    }


    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with ID: {}", id);
                    return new RuntimeException("Category not found: " + id);
                });
    }
}
