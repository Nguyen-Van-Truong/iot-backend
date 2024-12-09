package com.example.iotbackend.exception;

/**
 * Custom exception for handling Conflict (409) errors.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
