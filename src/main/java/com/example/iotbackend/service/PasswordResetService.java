package com.example.iotbackend.service;

import com.example.iotbackend.model.PasswordReset;

import java.util.Optional;

/**
 * PasswordResetService defines operations related to PasswordReset entities.
 */
public interface PasswordResetService extends IService<PasswordReset, Long> {

    /**
     * Retrieves the most recent PasswordReset entry for a given account ID.
     *
     * @param accountId The ID of the account.
     * @return An Optional containing the latest PasswordReset if found, or empty otherwise.
     */
    Optional<PasswordReset> findTopByAccountIdOrderByExpirationTimeDesc(Long accountId);

    /**
     * Retrieves a PasswordReset entry by account ID and OTP.
     *
     * @param accountId The ID of the account.
     * @param otp The One-Time Password to verify.
     * @return An Optional containing the PasswordReset if found, or empty otherwise.
     */
    Optional<PasswordReset> findByAccountIdAndOtp(Long accountId, String otp);
}
