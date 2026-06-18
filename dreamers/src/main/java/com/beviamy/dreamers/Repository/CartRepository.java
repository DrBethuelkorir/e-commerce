package com.beviamy.dreamers.Repository;

import com.beviamy.dreamers.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {

}
