package com.example.iotbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * ErrorResponse is used to return error details to the client.
 * It contains status code, message, timestamp, and additional error details.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String details;
}
