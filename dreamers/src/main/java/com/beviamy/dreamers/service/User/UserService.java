package com.beviamy.dreamers.service.User;


import com.beviamy.dreamers.Dto.CartDto;
import com.beviamy.dreamers.Dto.CartItemsDto;
import com.beviamy.dreamers.Dto.UserDto;
import com.beviamy.dreamers.Repository.UserRepository;
import com.beviamy.dreamers.Request.createUserRequest;
import com.beviamy.dreamers.Request.updateUser;
import com.beviamy.dreamers.exeption.AlreadyexistsExeptiom;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.models.Cart;
import com.beviamy.dreamers.models.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    public final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override()
    public User findById(@PathVariable Long id) {
        return  userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public User createUser(createUserRequest request) {
        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyexistsExeptiom("User already exists with email: " + request.getEmail());
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        // Save and return
        return userRepository.save(user);
    }

    @Override
    public User updateUser(updateUser request, long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // Update the existing user with request data
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setEmail(request.getEmail());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete,
                        () -> {throw  new  ResourceNotFoundException("User Not Found");
                });

    }
    @Override
    public UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);

        // Manually convert Cart with its items
        if (user.getCart() != null) {
            CartDto cartDto = convertCartToDto(user.getCart());
            userDto.setCart(cartDto);
        }

        return userDto;
    }

    private CartDto convertCartToDto(Cart cart) {
        CartDto cartDto = modelMapper.map(cart, CartDto.class);

        // Convert CartItems
        if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            List<CartItemsDto> cartItemDtos = cart.getCartItems().stream()
                    .map(item -> modelMapper.map(item, CartItemsDto.class))
                    .collect(Collectors.toList());
            cartDto.setCartItemsDto(cartItemDtos);
        } else {
            cartDto.setCartItemsDto(new ArrayList<>());
        }

        return cartDto;
    }


    @Override
    public List<User> findall() {

        return  userRepository.findAll();
    }
    @Override
    @Transactional
    public UserDto getUserWithCart(Long userId) {
        User user = userRepository.findByIdWithCartItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found with ID: " + userId));
        return convertToDto(user);
    }
    @Transactional
    @Override
    public UserDto getUserWithOrders(Long userId) {
        User user = userRepository.findByIdWithOrders(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found with ID: " + userId));
        return convertToDto(user);
    }

}
