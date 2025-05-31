package com.ecommerce.product_service.service;

import com.ecommerce.product_service.model.Category;
import com.ecommerce.product_service.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category){
        if (categoryRepository.existsByName(category.getName())){
            throw new IllegalArgumentException("Category already exist: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Category updateCategory(Long id, Category category){
        Category existing = getCategoryById(id);

        existing.setName(category.getName());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id){
        Category existing = getCategoryById(id);
        categoryRepository.delete(existing);
    }
}
