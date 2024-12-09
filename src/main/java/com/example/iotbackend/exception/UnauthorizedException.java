package com.example.iotbackend.exception;

/**
 * Custom exception for handling Unauthorized (401) errors.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
