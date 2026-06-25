package com.beviamy.dreamers.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemsDto {
    private Long productId;
    private Long productName;
    private Long quantity;
    private BigDecimal price;

}
