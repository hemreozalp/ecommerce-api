package com.ecommerce.product_service.mapper;

import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.model.Category;
import com.ecommerce.product_service.model.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequest request, Category category){
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);
        return product;
    }

    public static ProductResponse toResponse(Product product){
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setId(product.getCategory().getId());
        categoryResponse.setName(product.getCategory().getName());
        response.setCategory(categoryResponse);

        return response;
    }
}
