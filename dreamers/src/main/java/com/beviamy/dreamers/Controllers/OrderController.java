package com.beviamy.dreamers.Controllers;

import com.beviamy.dreamers.APIResonse;
import com.beviamy.dreamers.Dto.OrderDto;
import com.beviamy.dreamers.Repository.UserRepository;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.models.Order;
import com.beviamy.dreamers.service.Order.IOrderService;
import com.beviamy.dreamers.service.Order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/order")
public class OrderController {
    public final IOrderService orderService;

    @PostMapping("/createorder")
    public ResponseEntity<APIResonse> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new APIResonse("Order created", order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error", e.getMessage()));
        }
    }
    @GetMapping("/{orderId}/order")
    public ResponseEntity<APIResonse> getOrderById(@RequestParam Long orderId) {
        try {
            OrderDto order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(new APIResonse("Order found", order));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("Order not found", e.getMessage()));
        }

    }
    @GetMapping("/{userId}UserOrder")
    public ResponseEntity<APIResonse> getUserOrders(@RequestParam Long userId) {
        try {
            List<OrderDto> order = orderService.findByUserId(userId);
            return ResponseEntity.ok(new APIResonse("Order found", order));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("Order not found", e.getMessage()));
        }
    }
}
