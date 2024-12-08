package com.example.iotbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * VerifyOtpRequest represents the data required to verify the OTP sent to the user's email.
 * It contains the user's email and the OTP.
 */
@Getter
@Setter
public class VerifyOtpRequest {

    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "OTP must not be blank")
    private String otp;
}
