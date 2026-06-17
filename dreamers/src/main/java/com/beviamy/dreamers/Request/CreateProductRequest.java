package com.beviamy.dreamers.Request;

import com.beviamy.dreamers.models.Category;

import lombok.Data;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private String brand;
    private double price;
    private Long quantity;
    private String category;
}

