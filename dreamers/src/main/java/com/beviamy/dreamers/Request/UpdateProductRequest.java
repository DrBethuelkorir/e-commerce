package com.beviamy.dreamers.Request;

import com.beviamy.dreamers.models.Category;
import lombok.Data;

@Data
public class UpdateProductRequest {
    private String brand;
    private double price;
    private Long quantity;
    private String description;
    private Category category;
}

