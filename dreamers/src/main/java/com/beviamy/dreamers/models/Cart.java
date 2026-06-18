package com.beviamy.dreamers.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItems> cartItems = new HashSet<>();

    private void updateTotalAmount() {
        if (cartItems == null || cartItems.isEmpty()) {
            this.totalAmount = BigDecimal.ZERO;
            return;
        }

        this.totalAmount = cartItems.stream()
                .map(item -> {
                    BigDecimal unitPrice = item.getUnitPrice();
                    if (unitPrice == null) {
                        return BigDecimal.ZERO;
                    }
                    return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(CartItems item) {
        if (cartItems == null) {
            cartItems = new HashSet<>();
        }
        this.cartItems.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    public void removeItem(CartItems item) {
        if (cartItems != null) {
            this.cartItems.remove(item);
            item.setCart(null);
            updateTotalAmount();
        }
    }

    public void removeItemByProductId(Long productId) {
        if (cartItems != null) {
            cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
            updateTotalAmount();
        }
    }

    public void updateItemQuantity(Long productId, int newQuantity) {
        if (cartItems != null) {
            cartItems.stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .ifPresent(item -> {
                        item.setQuantity(newQuantity);
                        item.calculateTotalPrice();
                        updateTotalAmount();
                    });
        }
    }

    public void clearCart() {
        if (cartItems != null) {
            cartItems.clear();
            updateTotalAmount();
        }
    }
}