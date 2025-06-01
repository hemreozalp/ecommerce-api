package com.ecommerce.product_service.mapper;

import com.ecommerce.product_service.dto.CategoryRequest;
import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.model.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryRequest request){
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    public static CategoryResponse toResponse(Category category){
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }
}
