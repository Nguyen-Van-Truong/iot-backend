package com.example.iotbackend.service;

/**
 * JwtService is an interface that defines the operations related to JSON Web Token (JWT) generation, validation,
 * and extraction. This service is used for handling authentication and authorization in the system via JWT tokens.
 */
public interface JwtService {

    /**
     * Generates a JWT token for the given username.
     *
     * @param username The username for which the token is being generated.
     * @return The generated JWT token.
     */
    String generateToken(String username);

    /**
     * Validates the given JWT token to check if it's properly signed and not expired.
     *
     * @param token The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    boolean validateToken(String token);

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token The JWT token from which the username will be extracted.
     * @return The username (subject) extracted from the token.
     */
    String extractUsername(String token);
}
