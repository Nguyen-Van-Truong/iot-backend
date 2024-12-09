package com.example.iotbackend.controller;

import com.example.iotbackend.dto.request.*;
import com.example.iotbackend.dto.response.*;
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
 * This includes user login, user registration, and password reset processes.
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
        String token = accountService.authenticate(loginRequest);  // Attempt to authenticate the user and generate a token
        return ResponseEntity.ok(new LoginResponse("Login successful", token));
    }

    /**
     * Endpoint for user registration.
     *
     * @param registerRequest - The request containing the new user's registration information (email, password, etc.).
     * @return ResponseEntity - A response containing the registration status and a success message if successful.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = accountService.register(registerRequest);  // Attempt to register a new user
        return ResponseEntity.status(HttpStatus.CREATED).body(response);  // Respond with 201 Created if registration is successful
    }

    /**
     * Endpoint to initiate the password reset process.
     * It generates an OTP and sends it to the user's email.
     *
     * @param forgotPasswordRequest The request containing the user's email.
     * @return ResponseEntity containing a success message if the OTP is sent successfully.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        ForgotPasswordResponse response = accountService.initiateForgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to verify the OTP sent to the user's email.
     *
     * @param verifyOtpRequest The request containing the user's email and OTP.
     * @return ResponseEntity containing a success message if the OTP is verified successfully.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        VerifyOtpResponse response = accountService.verifyOtp(verifyOtpRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to reset the user's password after successful OTP verification.
     *
     * @param resetPasswordRequest The request containing the user's email  , OTP, and new password.
     * @return ResponseEntity containing a success message if the password is reset successfully.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordResponse response = accountService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok(response);
    }
}
