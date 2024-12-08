package com.example.iotbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * ResetPasswordRequest represents the data required to reset the user's password.
 * It contains the user's email, the OTP for verification, and the new password.
 */
@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "OTP must not be blank")
    private String otp;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;
}
