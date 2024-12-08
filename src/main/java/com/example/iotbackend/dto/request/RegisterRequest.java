package com.example.iotbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * RegisterRequest represents the data required for user registration.
 * It includes fields for email, password, full name, and optionally a phone number.
 * The fields are validated with constraints like not blank, valid email format, and minimum password length.
 */
@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Full name must not be blank")
    private String fullName;

    private String phoneNumber;
}
