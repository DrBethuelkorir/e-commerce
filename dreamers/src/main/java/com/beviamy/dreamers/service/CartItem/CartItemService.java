package com.beviamy.dreamers.service.CartItem;

import com.beviamy.dreamers.Repository.CartItemRepository;
import com.beviamy.dreamers.Repository.CartRepository;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.models.Cart;
import com.beviamy.dreamers.models.CartItems;
import com.beviamy.dreamers.models.Product;
import com.beviamy.dreamers.service.Cart.ICartService;
import com.beviamy.dreamers.service.products.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{

    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long ProductId, int Quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(ProductId);
        CartItems cartItems = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId()
                        .equals(product.getId()))
                .findFirst().orElse(new  CartItems());

        if(cartItems.getId() == null){
            cartItems.setCart(cart);
            cartItems.setProduct(product);
            cartItems.setQuantity(Quantity);
            cartItems.setUnitPrice(product.getPrice());
        }
        else{
            cartItems.setQuantity(cartItems.getQuantity() + Quantity);
        }
        cartItems.calculateTotalPrice();
        cart.addItem(cartItems);
        cartItemRepository.save(cartItems);
        cartRepository.save(cart);

    }

    @Override
    public void removeItemFromCart(Long cardId, Long ProductId) {
        Cart cart = cartService.getCart(cardId);
        CartItems itemstoberemoved = getcartitem(cardId, ProductId);
        cart.removeItem(itemstoberemoved);
        cartRepository.save(cart);
    }
    @Override
    public void updateCartItemQuantity(Long cardId, Long ProductId,int quantity) {
        Cart cart = cartService.getCart(cardId);
        cart.getCartItems().stream()
                .filter(item -> item.getProduct()
                        .getId().equals(ProductId))
                .findFirst().ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(productService.getProductById(ProductId)
                            .getPrice());
                    item.calculateTotalPrice();
                });
        BigDecimal Totalprice = cart.getTotalAmount();
        cart.setTotalAmount(Totalprice);
        cartRepository.save(cart);
    }

    @Override
    public CartItems getcartitem(Long cardId, Long ProductId) {
        Cart cart = cartService.getCart(cardId);
        return cart.getCartItems().stream().filter(item -> item.getProduct()
                .getId().equals(ProductId))
                .findFirst().orElseThrow(() ->new ResourceNotFoundException("item not found"));
    }
}
