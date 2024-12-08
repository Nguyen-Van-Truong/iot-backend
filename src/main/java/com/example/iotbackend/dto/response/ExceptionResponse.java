package com.example.iotbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * ExceptionResponse is used to return exception details.
 * It includes the exception message and timestamp of the error.
 */
@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse {
    private String message;
    private LocalDateTime timestamp;

    public ExceptionResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
