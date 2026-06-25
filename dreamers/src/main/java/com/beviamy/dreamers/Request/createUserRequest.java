package com.beviamy.dreamers.Request;

import lombok.Data;

@Data
public class createUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
