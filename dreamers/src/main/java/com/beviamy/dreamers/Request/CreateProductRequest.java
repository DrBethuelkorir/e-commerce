package com.beviamy.dreamers.Request;

import com.beviamy.dreamers.models.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    private String name;
    private String brand;
    private BigDecimal price;
    private Long quantity;
    private String description;
    private String category;  // ← Add this
}