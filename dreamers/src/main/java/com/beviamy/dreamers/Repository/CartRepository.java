package com.beviamy.dreamers.Repository;

import com.beviamy.dreamers.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);

    // ADD THIS NEW METHOD ↓
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.user.id = :userId")
    Optional<Cart> findByUserIdWithItems(@Param("userId") Long userId);

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.id = :id")
    Optional<Cart> findByIdWithItems(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Cart c SET c.totalAmount = :total WHERE c.id = :id")
    void updateTotalAmountById(@Param("id") Long id, @Param("total") BigDecimal total);
}