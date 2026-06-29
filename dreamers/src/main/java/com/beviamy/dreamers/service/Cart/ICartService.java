package com.beviamy.dreamers.service.Cart;

import com.beviamy.dreamers.models.Cart;
import com.beviamy.dreamers.models.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void cleanCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart cartInitializer(User user);

    Cart getCartByUserId(Long userId);
}
