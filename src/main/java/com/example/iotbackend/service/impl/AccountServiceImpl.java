package com.example.iotbackend.service.impl;

import com.example.iotbackend.dto.request.LoginRequest;
import com.example.iotbackend.dto.request.RegisterRequest;
import com.example.iotbackend.dto.response.AccountResponse;
import com.example.iotbackend.dto.response.RegisterResponse;
import com.example.iotbackend.exception.BadRequestException;
import com.example.iotbackend.exception.ResourceNotFoundException;
import com.example.iotbackend.model.Account;
import com.example.iotbackend.repository.AccountRepository;
import com.example.iotbackend.service.AccountService;
import com.example.iotbackend.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AccountServiceImpl is the service layer implementation for managing user accounts.
 * It provides methods for user authentication, registration, account retrieval, and password management.
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends AbstractService<Account, Long> implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Returns the repository for performing CRUD operations on Account entities.
     */
    @Override
    protected JpaRepository<Account, Long> getRepository() {
        return accountRepository;
    }

    /**
     * Retrieves all accounts from the database.
     * @return A list of all accounts.
     * @throws ResourceNotFoundException if no accounts are found in the database.
     */
    @Override
    public List<Account> findAll() {
        List<Account> accounts = getRepository().findAll();
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found in the database.");
        }
        return accounts;
    }

    /**
     * Retrieves account details by its ID.
     * @param id The ID of the account to retrieve.
     * @return An AccountResponse object containing the account details.
     * @throws ResourceNotFoundException if the account with the given ID is not found.
     */
    @Override
    public AccountResponse getAccountResponseById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        return new AccountResponse(
                account.getId(),
                account.getEmail(),
                account.getFullName(),
                account.getPhoneNumber(),
                account.getRoleId(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }

    /**
     * Saves an account to the database after encoding the password.
     * @param account The account to be saved.
     * @return The saved account.
     */
    @Override
    @Transactional
    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));  // Encrypt password before saving
        return getRepository().save(account);
    }

    /**
     * Authenticates a user based on the provided login request (email and password).
     * If authentication is successful, a JWT token is generated.
     * @param loginRequest The login credentials (email and password).
     * @return A JWT token if authentication is successful.
     * @throws BadRequestException if authentication fails (invalid username or password).
     */
    @Override
    public String authenticate(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            return jwtService.generateToken(loginRequest.getEmail());  // Generate JWT token for authenticated user
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid username or password");  // Throw exception if authentication fails
        }
    }

    /**
     * Registers a new user account.
     * Checks if the email is already in use and throws an exception if so.
     * @param registerRequest The registration details.
     * @return A RegisterResponse indicating success and the ID of the newly created account.
     * @throws BadRequestException if the email is already in use.
     */
    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        // Check if the email is already taken
        if (accountRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already in use");
        }

        // Create a new account with the provided details
        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        account.setPassword(registerRequest.getPassword());
        account.setFullName(registerRequest.getFullName());
        account.setPhoneNumber(registerRequest.getPhoneNumber());
        account.setRoleId(1L);  // Default role ID, can be changed later

        Account savedAccount = save(account);  // Save the new account to the database
        return new RegisterResponse("Registration successful", savedAccount.getId());  // Return response with account ID
    }

}
