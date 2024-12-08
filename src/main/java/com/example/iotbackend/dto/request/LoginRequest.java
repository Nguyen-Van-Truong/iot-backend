package com.example.iotbackend.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * LoginRequest represents the data required for user login.
 * It contains the email and password fields submitted by the client for authentication.
 */
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}
