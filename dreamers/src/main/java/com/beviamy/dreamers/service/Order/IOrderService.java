package com.beviamy.dreamers.service.Order;

import com.beviamy.dreamers.models.Order;

public interface IOrderService {
    Order placeOrder(Long userId);
    Order getOrderById(Long id);
}
