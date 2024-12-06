package com.example.iotbackend.security;

import com.example.iotbackend.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Custom implementation of AuthenticationEntryPoint to handle unauthorized access attempts
 * with invalid JWT tokens.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    // Constructor to inject ObjectMapper to serialize the ErrorResponse object into JSON format
    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * This method is called when an unauthenticated user tries to access a protected resource.
     * It sends a 401 Unauthorized response with an error message in JSON format.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param authException the authentication exception
     * @throws IOException if there is an error in writing the response
     * @throws ServletException if the servlet encounters an error
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String message = "Invalid JWT token";

        // Create an error response object to be sent in the response
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                message,
                LocalDateTime.now(),
                request.getRequestURI()
        );

        // Set the response type as JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Set the HTTP status to 401 Unauthorized
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // Serialize the error response object to JSON and write it to the output stream
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
