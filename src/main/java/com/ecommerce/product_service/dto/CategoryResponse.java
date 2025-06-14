package com.ecommerce.product_service.dto;

public class CategoryResponse {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
