package com.beviamy.dreamers.service.Order;

import com.beviamy.dreamers.Dto.OrderDto;
import com.beviamy.dreamers.Dto.OrderItemsDto;
import com.beviamy.dreamers.Repository.OrderRepository;
import com.beviamy.dreamers.Repository.ProductRepository;
import com.beviamy.dreamers.enums.OrderStatus;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.models.Cart;
import com.beviamy.dreamers.models.Order;
import com.beviamy.dreamers.models.OrderItems;
import com.beviamy.dreamers.models.Product;
import com.beviamy.dreamers.service.Cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    public final OrderRepository orderRepository;
    public final ProductRepository productRepository;
    public final CartService cartService;

    @Override
    @Transactional
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        // Check if cart has items
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty! Cannot place order.");
        }

        Order order = createOrder(cart);
        List<OrderItems> orderItemsList = createOrderItems(order, cart);

        order.setOrderItems(new HashSet<>(orderItemsList));
        order.setTotalAmount(calculateTotalFromOrderItems(orderItemsList));
        Order savedOrder = orderRepository.save(order);

        cartService.cleanCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItems> createOrderItems(Order order, Cart cart) {
        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItems(
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice(),
                    order,
                    product
            );
        }).toList();
    }

    private BigDecimal calculateTotalFromOrderItems(List<OrderItems> orderItems) {
        return orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<OrderDto> findByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("Order not found");
        }
        return orders.stream()
                .map(this::convertToDto)
                .toList();
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getOrderStatus());

        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
        }

        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            List<OrderItemsDto> itemDtos = order.getOrderItems().stream()
                    .map(this::convertToOrderItemDto)
                    .toList();
            dto.setOrderItems(itemDtos);
        }

        return dto;
    }

    private OrderItemsDto convertToOrderItemDto(OrderItems item) {
        OrderItemsDto dto = new OrderItemsDto();

        // FIX: Get Product ID from the product, not from the order item
        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
        }

        dto.setQuantity((long) item.getQuantity());
        dto.setPrice(item.getUnitPrice());
        return dto;
    }
}