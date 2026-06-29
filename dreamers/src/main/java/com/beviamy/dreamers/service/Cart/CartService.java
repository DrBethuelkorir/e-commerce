package com.beviamy.dreamers.service.Cart;

import com.beviamy.dreamers.Repository.CartItemRepository;
import com.beviamy.dreamers.Repository.CartRepository;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.models.Cart;
import com.beviamy.dreamers.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalPrice = cart.getTotalAmount();
        cart.setTotalAmount(totalPrice);
    return cartRepository.save(cart);
    }

    @Override
    public void cleanCart(Long id) {
        Cart cart =  getCart(id);
        cartItemRepository.deleteAllBycartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart  = getCart(id);
        return cart.getTotalAmount();
    }
    @Override
    public Cart cartInitializer(User user){
        return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(() ->{
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElse(null);
    }
}
