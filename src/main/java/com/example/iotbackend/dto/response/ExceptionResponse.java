package com.example.iotbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
