package com.example.iotbackend.exception;

/**
 * Custom exception for handling Resource Not Found (404) errors.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
