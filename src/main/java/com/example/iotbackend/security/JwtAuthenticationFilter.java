package com.example.iotbackend.security;

import com.example.iotbackend.dto.response.ErrorResponse;
import com.example.iotbackend.exception.BadRequestException;
import com.example.iotbackend.service.JwtService;
import com.example.iotbackend.service.impl.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This class is a filter that intercepts every HTTP request to check for a valid JWT token
 * and sets the authentication in the Spring Security context if the token is valid.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    private final JwtService jwtService;                // Service to handle JWT token extraction and validation
    private final CustomUserDetailsService userDetailsService;  // Service to load user details based on the username (email)

    /**
     * This method is called for every incoming HTTP request.
     * It checks for the presence of a valid JWT token in the Authorization header.
     * If the token is found, it extracts the user information (email), validates the token,
     * and sets the authentication in the Spring Security context.
     * If the token is invalid or expired, an exception is thrown and the request is rejected with a 401 Unauthorized status.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);  // Get the Authorization header from the request
        final String jwt;

        // If there is no Authorization header or it doesn't start with "Bearer ", proceed with the next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);  // Extract the JWT token from the header (after "Bearer ")

        try {
            // Extract username from the JWT token
            String email = jwtService.extractUsername(jwt);
            System.out.println("extractUsername: " + email);

            // If email is valid and there is no authentication yet in the SecurityContext
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details using the email extracted from the token
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                if (jwtService.validateToken(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // Invalid JWT token
                    throw new BadCredentialsException("Invalid JWT token");
                }
            }

            filterChain.doFilter(request, response);

        } catch (BadCredentialsException ex) {
            // Create the ErrorResponse object
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ex.getMessage(),
                    LocalDateTime.now(),
                    request.getRequestURI()
            );

            // Set response status and write the error response in JSON format
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));  // Use Jackson to convert to JSON
        }
    }

}
