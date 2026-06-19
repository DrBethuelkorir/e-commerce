package com.beviamy.dreamers.service.CartItem;

import com.beviamy.dreamers.models.CartItems;

public interface ICartItemService {
    void addItemToCart(Long cardId, Long ProductId, int Quantity);
    void removeItemFromCart(Long cardId, Long ProductId);
    void updateCartItemQuantity(Long cardId, Long ProductId, int Quantity);

    CartItems getcartitem(Long cardId, Long ProductId);
}
