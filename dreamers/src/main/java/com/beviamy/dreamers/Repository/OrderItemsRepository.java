package com.beviamy.dreamers.Repository;

import com.beviamy.dreamers.models.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
}
