package com.beviamy.dreamers.Dto;

import com.beviamy.dreamers.models.Cart;
import com.beviamy.dreamers.models.CartItems;
import com.beviamy.dreamers.models.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDto {
    private Long id;
    private BigDecimal totalAmount;
    private List<CartItemsDto> cartItemsDto;
}
