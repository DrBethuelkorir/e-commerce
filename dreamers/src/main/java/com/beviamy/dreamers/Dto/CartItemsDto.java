package com.beviamy.dreamers.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemsDto {
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
