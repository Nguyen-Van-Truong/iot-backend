package com.example.iotbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * ForgotPasswordRequest represents the data required to initiate a password reset.
 * It contains the user's email address.
 */
@Getter
@Setter
public class ForgotPasswordRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;
}
