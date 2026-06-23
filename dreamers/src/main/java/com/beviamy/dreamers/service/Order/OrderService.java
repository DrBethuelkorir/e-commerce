package com.beviamy.dreamers.service.Order;

import com.beviamy.dreamers.Repository.CartRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    public final OrderRepository orderRepository;
    public final ProductRepository productRepository;
    public final CartService cartService;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItems> orderItemsList = createOrderItems(order, cart);

        order.setOrderItems(new HashSet<>(orderItemsList));
        order.setTotalAmount(getTotalPrice((Cart) orderItemsList));
        Order Savedorder =  orderRepository.save(order);

        cartService.cleanCart(cart.getId());
        return Savedorder;
    }
    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return orderRepository.save(order);
    }
    private List<OrderItems> createOrderItems(Order order, Cart cart) {
        return cart.getCartItems().stream().map(cartitem ->{
            Product product = cartitem.getProduct();
            product.setQuantity(product.getQuantity()-cartitem.getQuantity());
            productRepository.save(product);
            return new OrderItems(
                    cartitem.getQuantity(),
                    cartitem.getUnitPrice(),
                    order,
                    product

            );
        }).toList();

    }

    public BigDecimal getTotalPrice(Cart cart) {
    return cart.getCartItems()
                .stream()
                .map(cartitem  ->
            cartitem.getTotalPrice()
                    .multiply(BigDecimal.valueOf(cartitem.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        }


    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUSerId(userId);
    }
}
