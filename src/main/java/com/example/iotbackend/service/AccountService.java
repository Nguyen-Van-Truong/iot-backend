package com.example.iotbackend.service;

import com.example.iotbackend.dto.request.*;
import com.example.iotbackend.dto.response.*;
import com.example.iotbackend.model.Account;

/**
 * AccountService is the service interface that defines the operations related to user accounts.
 * It extends IService to provide basic CRUD operations for managing accounts, and adds
 * additional functionality specific to account authentication and registration.
 * including authentication, registration, and password reset functionalities.
 */
public interface AccountService extends IService<Account, Long> {

    /**
     * Retrieves an AccountResponse containing account details by account ID.
     *
     * @param id The ID of the account to retrieve.
     * @return AccountResponse The account details in a response format.
     */
    AccountResponse getAccountResponseById(Long id);

    /**
     * Authenticates the user based on provided login credentials (email and password).
     * It generates a JWT token upon successful authentication.
     *
     * @param loginRequest Contains the user's email and password for authentication.
     * @return A JWT token if authentication is successful.
     * @throws AuthenticationException if the credentials are invalid.
     */
    String authenticate(LoginRequest loginRequest);

    /**
     * Registers a new account by taking the user's registration details.
     * It validates the input, creates a new Account, and returns a success message.
     *
     * @param registerRequest The details of the user to register.
     * @return RegisterResponse A response message confirming the successful registration.
     * @throws BadRequestException if the registration fails due to invalid or duplicate data.
     */
    RegisterResponse register(RegisterRequest registerRequest);

    /**
     * Initiates the password reset process by generating an OTP and sending it to the user's email.
     *
     * @param forgotPasswordRequest The request containing the user's email.
     * @return ForgotPasswordResponse A response message indicating that the OTP has been sent.
     */
    ForgotPasswordResponse initiateForgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    /**
     * Verifies the OTP provided by the user.
     *
     * @param verifyOtpRequest The request containing the user's email and OTP.
     * @return VerifyOtpResponse A response message indicating the result of the OTP verification.
     */
    VerifyOtpResponse verifyOtp(VerifyOtpRequest verifyOtpRequest);

    /**
     * Resets the user's password after successful OTP verification.
     *
     * @param resetPasswordRequest The request containing the user's email, OTP, and new password.
     * @return ResetPasswordResponse A response message confirming the password reset.
     */
    ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest);
}
