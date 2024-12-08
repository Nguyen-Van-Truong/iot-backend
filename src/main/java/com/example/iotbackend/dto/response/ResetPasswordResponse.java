package com.example.iotbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * ResetPasswordResponse is used to acknowledge the successful reset of a password.
 * It contains a message indicating that the password has been updated.
 */
@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordResponse {
    private String message;
}
