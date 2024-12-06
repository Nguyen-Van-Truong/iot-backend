package com.example.iotbackend.controller;

import com.example.iotbackend.dto.request.LoginRequest;
import com.example.iotbackend.dto.request.RegisterRequest;
import com.example.iotbackend.dto.response.LoginResponse;
import com.example.iotbackend.dto.response.RegisterResponse;
import com.example.iotbackend.dto.response.ExceptionResponse;
import com.example.iotbackend.exception.BadRequestException;
import com.example.iotbackend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * AuthenticationController is responsible for handling authentication-related requests.
 * This includes user login and user registration processes.
 * The controller communicates with the AccountService to authenticate users and register new accounts.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AccountService accountService;

    /**
     * Endpoint for user login.
     *
     * @param loginRequest - The request containing the user's login credentials (email and password).
     * @return ResponseEntity - A response containing the login status and an authentication token if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = accountService.authenticate(loginRequest);  // Attempt to authenticate the user and generate a token
            return ResponseEntity.ok(new LoginResponse("Login successful", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)  // Respond with 401 Unauthorized if authentication fails
                    .body(new ExceptionResponse("Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)  // Respond with 400 Bad Request for other exceptions
                    .body(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Endpoint for user registration.
     *
     * @param registerRequest - The request containing the new user's registration information (email, password, etc.).
     * @return ResponseEntity - A response containing the registration status and a success message if successful.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            RegisterResponse response = accountService.register(registerRequest);  // Attempt to register a new user
            return ResponseEntity.status(HttpStatus.CREATED).body(response);  // Respond with 201 Created if registration is successful
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)  // Respond with 400 Bad Request if registration fails due to a bad request
                    .body(new ExceptionResponse(e.getMessage()));
        }
    }
}
