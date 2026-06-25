package com.beviamy.dreamers.service.Order;

import com.beviamy.dreamers.Dto.OrderDto;
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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public final ModelMapper modelMapper;

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
        return cart.getCartItems().stream().map(cartilage ->{
            Product product = cartilage.getProduct();
            product.setQuantity(product.getQuantity()- cartilage.getQuantity());
            productRepository.save(product);
            return new OrderItems(
                    cartilage.getQuantity(),
                    cartilage.getUnitPrice(),
                    order,
                    product

            );
        }).toList();

    }

    public BigDecimal getTotalPrice(Cart cart) {
    return cart.getCartItems()
                .stream()
                .map(cartilage ->
            cartilage.getTotalPrice()
                    .multiply(BigDecimal.valueOf(cartilage.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id).map(this :: convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        }



        public List<OrderDto> findByUserId(Long userId) {
           List<Order> order = orderRepository.findByUserId(userId);

           if(order.isEmpty()){
               throw new ResourceNotFoundException("Order not found");
           }
           return order.stream()
                   .map(this :: convertToDto)
                   .toList();
        }
    private OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
