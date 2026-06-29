package com.beviamy.dreamers.Controllers;

import com.beviamy.dreamers.APIResonse;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.models.Cart;
import com.beviamy.dreamers.models.User;
import com.beviamy.dreamers.service.Cart.ICartService;
import com.beviamy.dreamers.service.CartItem.ICartItemService;
import com.beviamy.dreamers.service.User.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartitem")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final IUserService userService;
    private final ICartService cartService;

    @PostMapping("/items/add")
    public ResponseEntity<APIResonse> addItemsToTheCart(

            @RequestParam long productId,
            @RequestParam int quantity
    ){
        try {

            User user = userService.findById(1L);
            Cart cart = cartService.cartInitializer(user);

            cartItemService.addItemToCart(cart.getId(),productId,quantity);
            return ResponseEntity.ok(new APIResonse("product added successfully",true));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("not found",e.getMessage()));
        }
    }
    @DeleteMapping("item/delete")
    public ResponseEntity<APIResonse> deleteItemFromTheCart(
            @RequestParam long cartId,
            @RequestParam long productId
    ){
        try {
            cartItemService.removeItemFromCart(cartId,productId);
            return ResponseEntity.ok(new APIResonse("success",true));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse(e.getMessage(),null));
        }

    }
    @PutMapping("/cart/{cartId}/productid/{productId}/update")
    public ResponseEntity<APIResonse> updateItemInCart(
            @PathVariable long cartId,
            @PathVariable long productId,
            @RequestParam int quantity
    ){
        try {
            cartItemService.updateCartItemQuantity(cartId,productId,quantity);
            return ResponseEntity.ok(new APIResonse("success",true));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse(e.getMessage(),null));
        }
    }


}
