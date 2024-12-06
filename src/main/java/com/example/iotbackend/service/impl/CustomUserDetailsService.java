package com.example.iotbackend.service.impl;

import com.example.iotbackend.model.Account;
import com.example.iotbackend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService is a custom implementation of the UserDetailsService interface
 * for loading user-specific data during authentication based on the email.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    /**
     * Loads the user by email for authentication purposes.
     * It fetches the user from the database and returns a UserDetails object containing
     * the user credentials (email, password) and roles.
     *
     * @param email The email of the user to be authenticated.
     * @return A UserDetails object representing the user.
     * @throws UsernameNotFoundException if no user is found with the given email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch the account from the database by email
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Return a UserDetails object (used by Spring Security for authentication)
        return User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .roles("USER")
                .build();
    }
}