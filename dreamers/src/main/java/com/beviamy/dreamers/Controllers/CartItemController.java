package com.beviamy.dreamers.Controllers;

import com.beviamy.dreamers.APIResonse;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.service.CartItem.ICartItemService;
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

    @GetMapping("/items/add")
    public ResponseEntity<APIResonse> addItemsToTheCart(
            @RequestParam long cartId,
            @RequestParam long productId,
            @RequestParam int quantity
    ){
        try {
            cartItemService.addItemToCart(cartId,productId,quantity);
            return ResponseEntity.ok(new APIResonse("success",true));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("not found",false));
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
