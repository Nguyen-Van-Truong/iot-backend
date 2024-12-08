package com.example.iotbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * VerifyOtpResponse is used to acknowledge the verification of an OTP.
 * It contains a message indicating whether the OTP is valid.
 */
@Getter
@Setter
@AllArgsConstructor
public class VerifyOtpResponse {
    private String message;
}
