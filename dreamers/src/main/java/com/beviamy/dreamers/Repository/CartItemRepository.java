package com.beviamy.dreamers.Repository;

import com.beviamy.dreamers.models.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItems,Long> {
    void deleteAllBycartId(Long id);
}
