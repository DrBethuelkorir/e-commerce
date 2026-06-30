package com.beviamy.dreamers.Repository;

import com.beviamy.dreamers.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.cart c LEFT JOIN FETCH c.cartItems ci WHERE u.id = :userId")
    Optional<User> findByIdWithCartItems(@Param("userId") Long userId);

    // FIX: Use "orders" (plural) to match the field name in User.java
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders o LEFT JOIN FETCH o.orderItems WHERE u.id = :userId")
    Optional<User> findByIdWithOrders(@Param("userId") Long userId);
}