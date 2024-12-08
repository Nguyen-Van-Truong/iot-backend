package com.example.iotbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * RegisterResponse is used to return the response after a successful user registration.
 * It contains a message indicating success and the newly created user's ID.
 */
@Getter
@Setter
@AllArgsConstructor
public class RegisterResponse {
    private String message;
    private Long userId;
}
