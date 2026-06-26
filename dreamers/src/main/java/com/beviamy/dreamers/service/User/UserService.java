package com.beviamy.dreamers.service.User;


import com.beviamy.dreamers.Dto.UserDto;
import com.beviamy.dreamers.Repository.UserRepository;
import com.beviamy.dreamers.Request.createUserRequest;
import com.beviamy.dreamers.Request.updateUser;
import com.beviamy.dreamers.exeption.AlreadyexistsExeptiom;
import com.beviamy.dreamers.exeption.ResourceNotFoundException;
import com.beviamy.dreamers.models.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    public final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User findById(Long id) {
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
    public UserDto convertToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<User> findall() {
        return  userRepository.findAll();
    }

}
