package com.beviamy.dreamers.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemsDto {
    private Long productId;
    private String productName;
    private Long quantity;
    private BigDecimal price;

}
