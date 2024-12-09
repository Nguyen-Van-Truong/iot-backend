package com.example.iotbackend.service.impl;

import com.example.iotbackend.dto.request.*;
import com.example.iotbackend.dto.response.*;
import com.example.iotbackend.exception.BadRequestException;
import com.example.iotbackend.exception.ConflictException;
import com.example.iotbackend.exception.ResourceNotFoundException;
import com.example.iotbackend.exception.UnauthorizedException;
import com.example.iotbackend.model.Account;
import com.example.iotbackend.model.PasswordReset;
import com.example.iotbackend.repository.AccountRepository;
import com.example.iotbackend.service.AccountService;
import com.example.iotbackend.service.EmailService;
import com.example.iotbackend.service.JwtService;
import com.example.iotbackend.service.PasswordResetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

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
    private final EmailService emailService;
    private final PasswordResetService passwordResetService;

    /**
     * Returns the repository for performing CRUD operations on Account entities.
     *
     * @return The AccountRepository instance.
     */
    @Override
    protected JpaRepository<Account, Long> getRepository() {
        return accountRepository;
    }

    /**
     * Retrieves account details by its ID.
     *
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
     *
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
     *
     * @param loginRequest The login credentials (email and password).
     * @return A JWT token if authentication is successful.
     * @throws BadRequestException if authentication fails (invalid username or password).
     */
    @Override
    public String authenticate(LoginRequest loginRequest) {
        try {
            // Authenticate with user information
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            // Generate and return a JWT token upon successful authentication
            return jwtService.generateToken(loginRequest.getEmail());
        } catch (AuthenticationException e) {
            // Throw UnauthorizedException if authentication fails due to invalid credentials
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    /**
     * Registers a new user account.
     * Checks if the email is already in use and throws an exception if so.
     *
     * @param registerRequest The registration details.
     * @return A RegisterResponse indicating success and the ID of the newly created account.
     * @throws ConflictException if the email is already in use.
     */
    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        // Check if the email is already taken
        if (accountRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new ConflictException("Email is already in use");
        }

        // Create a new account with the provided details
        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        account.setPassword(registerRequest.getPassword());
        account.setFullName(registerRequest.getFullName());
        account.setPhoneNumber(registerRequest.getPhoneNumber());
        account.setRoleId(1L);  // Default role ID, can be changed later

        // Save the new account to the database
        Account savedAccount = save(account);
        // Return a response with a success message and the ID of the newly created account
        return new RegisterResponse("Registration successful", savedAccount.getId());
    }

    /**
     * Initiates the password reset process by generating an OTP and sending it to the user's email.
     *
     * @param forgotPasswordRequest The request containing the user's email.
     * @return ForgotPasswordResponse A response message indicating that the OTP has been sent.
     * @throws BadRequestException if no account is found with the provided email.
     */
    @Override
    public ForgotPasswordResponse initiateForgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.getEmail();
        // Retrieve the account associated with the provided email
        Optional<Account> accountOpt = accountRepository.findByEmail(email);

        if (accountOpt.isEmpty()) {
            throw new BadRequestException("No account found with the provided email.");
        }

        Account account = accountOpt.get();
        Long accountId = account.getId();

        // Generate a 6-digit OTP
        String otp = generateOtp();

        // Set OTP expiry time to 5 minutes from now
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        // Retrieve the latest PasswordReset entry for the account or create a new one if none exists
        PasswordReset passwordReset = passwordResetService.findTopByAccountIdOrderByExpirationTimeDesc(accountId)
                .orElse(new PasswordReset());
        passwordReset.setAccountId(accountId);
        passwordReset.setOtp(otp);
        passwordReset.setExpirationTime(expiryTime);
        passwordReset.setIsVerified(false);

        // Save or update the PasswordReset entry
        passwordResetService.save(passwordReset);

        // Send the generated OTP to the user's email
        emailService.sendOtpEmail(email, otp);

        // Return a response indicating that the OTP has been sent
        return new ForgotPasswordResponse("OTP has been sent to your email.");
    }

    /**
     * Verifies the OTP provided by the user.
     *
     * @param verifyOtpRequest The request containing the user's email and OTP.
     * @return VerifyOtpResponse A response message indicating the result of the OTP verification.
     * @throws BadRequestException if the OTP is invalid, expired, or has already been used.
     */
    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        String email = verifyOtpRequest.getEmail();
        String otp = verifyOtpRequest.getOtp();

        // Retrieve the account associated with the provided email
        Optional<Account> accountOpt = accountRepository.findByEmail(email);

        if (accountOpt.isEmpty()) {
            throw new BadRequestException("No account found with the provided email.");
        }

        Account account = accountOpt.get();
        Long accountId = account.getId();

        // Retrieve the PasswordReset entry matching the account ID and OTP
        Optional<PasswordReset> passwordResetOpt = passwordResetService.findByAccountIdAndOtp(accountId, otp);
        PasswordReset passwordReset = passwordResetOpt.orElseThrow(() -> new BadRequestException("Invalid OTP."));

        // Check if the OTP has expired
        if (passwordReset.getExpirationTime().isBefore(LocalDateTime.now())) {
            // Delete the expired OTP
            passwordResetService.delete(passwordReset.getId());
            throw new BadRequestException("OTP has expired. Please request a new one.");
        }

        // Check if the OTP has already been verified (used)
        if (Boolean.TRUE.equals(passwordReset.getIsVerified())) {
            // Delete the already used OTP
            passwordResetService.delete(passwordReset.getId());
            throw new BadRequestException("OTP has already been used.");
        }

        // Mark the OTP as verified
        passwordReset.setIsVerified(true);
        // Save the updated PasswordReset entry
        passwordResetService.save(passwordReset);

        // Return a response indicating that the OTP has been verified successfully
        return new VerifyOtpResponse("OTP verified successfully.");
    }

    /**
     * Resets the user's password after successful OTP verification.
     *
     * @param resetPasswordRequest The request containing the user's email, OTP, and new password.
     * @return ResetPasswordResponse A response message confirming the password reset.
     * @throws BadRequestException if the OTP is invalid, expired, or has not been verified.
     */
    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();
        String otp = resetPasswordRequest.getOtp();
        String newPassword = resetPasswordRequest.getNewPassword();

        // Retrieve the account associated with the provided email
        Optional<Account> accountOpt = accountRepository.findByEmail(email);

        if (accountOpt.isEmpty()) {
            throw new BadRequestException("No account found with the provided email.");
        }

        Account account = accountOpt.get();
        Long accountId = account.getId();

        // Retrieve the PasswordReset entry matching the account ID and OTP
        Optional<PasswordReset> passwordResetOpt = passwordResetService.findByAccountIdAndOtp(accountId, otp);
        PasswordReset passwordReset = passwordResetOpt.orElseThrow(() -> new BadRequestException("Invalid OTP."));

        // Check if the OTP has expired
        if (passwordReset.getExpirationTime().isBefore(LocalDateTime.now())) {
            passwordResetService.delete(passwordReset.getId());
            throw new BadRequestException("OTP has expired. Please request a new one.");
        }

        // Check if the OTP has been verified
        if (!Boolean.TRUE.equals(passwordReset.getIsVerified())) {
            throw new BadRequestException("OTP has not been verified. Please verify the OTP first.");
        }

        // Update the user's password with the new encoded password
        account.setPassword(passwordEncoder.encode(newPassword));
        // Save the updated account to the database
        accountRepository.save(account);

        passwordResetService.delete(passwordReset.getId());

        // Return a response indicating that the password has been reset successfully
        return new ResetPasswordResponse("Password has been reset successfully.");
    }

    /**
     * Generates a 6-digit numeric OTP.
     *
     * @return A 6-digit OTP as a String.
     */
    private static String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        // Generate a random integer between 100000 and 999999 to ensure a 6-digit OTP
        int otpInt = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otpInt);
    }
}
