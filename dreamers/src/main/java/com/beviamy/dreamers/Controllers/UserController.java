package com.beviamy.dreamers.Controllers;

import com.beviamy.dreamers.APIResonse;
import com.beviamy.dreamers.Dto.UserDto;
import com.beviamy.dreamers.Request.createUserRequest;
import com.beviamy.dreamers.Request.updateUser;
import com.beviamy.dreamers.exeption.AlreadyexistsExeptiom;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.models.User;
import com.beviamy.dreamers.service.User.IUserService;
import com.beviamy.dreamers.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{id}/user")
    public ResponseEntity<APIResonse> getUserById(@PathVariable Long id){
        try {
            UserDto userDto = userService.getUserWithCart(id);
            return ResponseEntity.ok(new APIResonse("User found", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("User not found", false));
        }
    }

    @GetMapping("{userId}/adduser")
    public ResponseEntity<APIResonse> getUserById(@PathVariable long userId) {
        try {
            UserDto userDto = userService.getUserWithCart(userId);
            return ResponseEntity.ok(new APIResonse("user found", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("user not found!",e.getMessage()));
        }
    }
    @PostMapping("/add/user")
    public ResponseEntity<APIResonse> addUser(@RequestBody createUserRequest req ) {
        try {
            User user = userService.createUser(req);
            UserDto uSerDto = userService.convertToDto(user);
            return ResponseEntity.ok(new APIResonse("user created", uSerDto));
        } catch (AlreadyexistsExeptiom e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new APIResonse("user already exists!",e.getMessage()));
        }
    }
    @PutMapping("/update/{userId}/user")
    public ResponseEntity<APIResonse> updateUser(@RequestBody updateUser req, @PathVariable long userId ) {
        try {
            User user = userService.updateUser(req, userId);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new APIResonse("user updated", userDto));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("user not found!",e.getMessage()));
        }
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<APIResonse> deleteUser(@PathVariable Long userId) {
        try {
            User deletedUser = userService.findById(userId);
            userService.deleteUser(userId);
            return ResponseEntity.ok(new APIResonse("User deleted successfully", deletedUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("User not found!", e.getMessage()));
        }
    }
    @GetMapping("/all")
    public ResponseEntity<APIResonse> getAllUsers() {
        try {
            List<User> users = userService.findall(); // Get list of users
            List<UserDto> userDtos = users.stream()  // Convert each user to DTO
                    .map(userService::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new APIResonse("users found", userDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error retrieving users", e.getMessage()));
        }
    }
}
