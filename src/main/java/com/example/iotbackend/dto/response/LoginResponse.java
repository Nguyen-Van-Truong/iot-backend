package com.example.iotbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * LoginResponse is used to return a response after a successful login.
 * It includes a success message and the generated authentication token.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private String token;
}
