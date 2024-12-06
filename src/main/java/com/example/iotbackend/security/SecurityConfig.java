package com.example.iotbackend.security;

import com.example.iotbackend.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig is the configuration class for Spring Security.
 * It configures authentication, authorization, and session management for the application.
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity  // Enables method-level security annotations like @PreAuthorize and @Secured
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;   // Custom service to load user details from the database
    private final JwtAuthenticationFilter jwtAuthenticationFilter;  // Custom JWT authentication filter
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;  // Handles authentication errors (e.g., invalid JWT)

    /**
     * Configures the security filter chain. It defines access rules, handles exceptions,
     * and sets session management policy to stateless (because JWT authentication is stateless).
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())

                // Configure access rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Allow unauthenticated access
//                        .requestMatchers("**").permitAll() // Allow unauthenticated access to all endpoints

                        .anyRequest().authenticated()) // Require authentication for any other requests

                // Handle authentication exceptions
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))

                // Set session management to stateless
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean definition for PasswordEncoder. This encoder is used to hash passwords.
     * BCrypt is a strong, adaptive hash function for password storage.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Using BCrypt algorithm to encode passwords
    }

    /**
     * Bean definition for AuthenticationManager. It provides authentication functionality
     * and is used by Spring Security to authenticate users.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();  // Get the default AuthenticationManager from Spring Security
    }
}
