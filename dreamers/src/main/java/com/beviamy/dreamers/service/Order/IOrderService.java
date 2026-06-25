package com.beviamy.dreamers.service.Order;

import com.beviamy.dreamers.Dto.OrderDto;
import com.beviamy.dreamers.models.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrderById(Long id);
    List<OrderDto> findByUserId(Long userId);
}
