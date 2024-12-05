package com.example.iotbackend.controller;

import com.example.iotbackend.dto.request.LoginRequest;
import com.example.iotbackend.dto.response.LoginResponse;
import com.example.iotbackend.dto.request.RegisterRequest;
import com.example.iotbackend.dto.response.RegisterResponse;
import com.example.iotbackend.dto.response.ExceptionResponse;
import com.example.iotbackend.exception.BadRequestException;
import com.example.iotbackend.model.Account;
import com.example.iotbackend.service.AccountService;
import com.example.iotbackend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(loginRequest.getEmail());
                return ResponseEntity.ok(new LoginResponse("Login successful", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ExceptionResponse("Invalid username or password"));
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ExceptionResponse("Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (accountService.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already in use");
        }

        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        account.setPassword(registerRequest.getPassword());
        account.setFullName(registerRequest.getFullName());
        account.setPhoneNumber(registerRequest.getPhoneNumber());
        account.setRoleId(1L);

        Account savedAccount = accountService.save(account);

        return ResponseEntity.ok(new RegisterResponse("Registration successful", savedAccount.getId()));
    }
}
