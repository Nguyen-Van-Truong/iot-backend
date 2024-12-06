package com.example.iotbackend.service.impl;

import com.example.iotbackend.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JwtServiceImpl is an implementation of JwtService that handles the creation, validation,
 * and extraction of information from JWT (JSON Web Tokens).
 * This service is used to manage JWTs for user authentication and authorization.
 */
@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;  // The secret key used for signing and verifying JWT tokens
    private final long jwtExpirationMs;  // The expiration time of JWT tokens (in milliseconds)

    /**
     * Constructor to initialize the JwtService with secret key and expiration time
     * from application properties.
     *
     * @param secret          The secret key for JWT signing
     * @param jwtExpirationMs The expiration time of the JWT token (in milliseconds)
     */
    public JwtServiceImpl(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expirationMs}") long jwtExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());  // Generate the secret key from the provided secret string
        this.jwtExpirationMs = jwtExpirationMs;  // Set the expiration time for JWT
    }

    /**
     * Generates a JWT token for the given username.
     *
     * @param username The username to be included in the JWT token.
     * @return A JWT token as a String.
     */
    @Override
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // Set the username as the subject of the JWT token
                .setIssuedAt(new Date())  // Set the issued date as the current date
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))  // Set the expiration date based on the configured expiration time
                .signWith(secretKey, SignatureAlgorithm.HS256)  // Sign the token with the secret key and HS256 algorithm
                .compact();  // Generate the compact JWT token
    }

    /**
     * Validates a JWT token to ensure it is not expired or tampered with.
     *
     * @param token The JWT token to be validated.
     * @return True if the token is valid, false if invalid or expired.
     */
    @Override
    public boolean validateToken(String token) {
        // Attempt to parse the token using the secret key and validate its signature and claims
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        return true;  // If no exceptions, the token is valid
    }

    /**
     * Extracts the username (subject) from the given JWT token.
     * <p>
     * This method parses the provided JWT token and extracts the subject (which typically represents the username or user ID).
     * If the token is invalid, expired, or the signature doesn't match, an exception will be thrown.
     *
     * @param token The JWT token from which the username will be extracted.
     * @return The username (subject) stored in the token.
     * @throws BadCredentialsException if the token is invalid, expired, or has an incorrect signature.
     */
    @Override
    public String extractUsername(String token) {
        try {
            // Parse the JWT token and extract the subject (username)
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            // Handle invalid JWT signature (the signature does not match)
            System.out.println("JWT signature invalid: " + e.getMessage());
            throw new BadCredentialsException("Invalid JWT token: signature mismatch", e);
        } catch (ExpiredJwtException e) {
            // Handle expired JWT token
            System.out.println("JWT token expired: " + e.getMessage());
            throw new BadCredentialsException("JWT token has expired", e);
        } catch (JwtException e) {
            // Handle other JWT parsing issue
            System.out.println("JWT parsing failed: " + e.getMessage());
            throw new BadCredentialsException("Invalid JWT token", e);
        }
    }

}
