package com.example.iotbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * ForgotPasswordResponse is used to acknowledge the receipt of a password reset request.
 * It contains a message indicating that the OTP has been sent.
 */
@Getter
@Setter
@AllArgsConstructor
public class ForgotPasswordResponse {
    private String message;
}
