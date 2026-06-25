package com.beviamy.dreamers.service.User;


import com.beviamy.dreamers.Dto.UserDto;
import com.beviamy.dreamers.Request.createUserRequest;
import com.beviamy.dreamers.Request.updateUser;
import com.beviamy.dreamers.models.User;

public interface IUserService {
    User findById(Long id);
    User createUser(createUserRequest request);
    User updateUser(updateUser request, long id);
    void deleteUser(Long id);

    UserDto convertToDto(User user);
}
