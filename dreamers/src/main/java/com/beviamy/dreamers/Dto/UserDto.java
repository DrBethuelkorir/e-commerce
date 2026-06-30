package com.beviamy.dreamers.Dto;

import com.beviamy.dreamers.models.Cart;
import com.beviamy.dreamers.models.Order;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private CartDto cart;
    private List<OrderDto> orders;
}
