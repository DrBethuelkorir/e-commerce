package com.beviamy.dreamers.Controllers;

import com.beviamy.dreamers.APIResonse;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.service.Cart.ICartService;
import com.beviamy.dreamers.service.CartItem.CartItemService;
import com.beviamy.dreamers.service.CartItem.ICartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart")
public class CartContoller {

    private final ICartService cartService;

    @GetMapping("/{cartId}/cart")
    public ResponseEntity<APIResonse> getCartById(@PathVariable long cartId){
        try {
            cartService.getCart(cartId);
            return ResponseEntity.ok(new APIResonse("Found", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("Not Found", null));
        }
    }
    @GetMapping("/total")
    public ResponseEntity<APIResonse> getTotalAmount(@RequestParam Long cartId){
        cartService.getTotalPrice(cartId);
        return ResponseEntity.ok(new APIResonse("Done", null));
    }
    @DeleteMapping("/{cartId}/delete")
    public ResponseEntity<APIResonse> deleteCart(@PathVariable Long cartId){
        cartService.cleanCart(cartId);
        return ResponseEntity.ok(new APIResonse("Deleted", null));
    }

}
