package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.CategoryRequest;
import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.model.Category;
import com.ecommerce.product_service.mapper.CategoryMapper;
import com.ecommerce.product_service.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category already exist: " + request.getName());
        }
        Category category = CategoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);
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
        return CategoryMapper.toResponse(updated);
    }

    public void deleteCategory(Long id) {
        Category existing = findCategoryById(id);
        categoryRepository.delete(existing);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));
    }
}
