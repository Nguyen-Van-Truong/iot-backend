package com.example.iotbackend.exception;

/**
 * Custom exception for handling Bad Request (400) errors.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
