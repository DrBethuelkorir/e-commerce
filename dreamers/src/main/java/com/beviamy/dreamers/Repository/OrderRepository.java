package com.beviamy.dreamers.Repository;

import com.beviamy.dreamers.models.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUSerId(Long userId);
}
