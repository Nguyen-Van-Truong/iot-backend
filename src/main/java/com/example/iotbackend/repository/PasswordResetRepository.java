package com.example.iotbackend.repository;

import com.example.iotbackend.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * PasswordResetRepository provides CRUD operations for PasswordReset entities.
 */
@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    
    /**
     * Finds a PasswordReset by account ID and OTP.
     *
     * @param accountId The account ID associated with the OTP.
     * @param otp The OTP code.
     * @return An Optional containing the PasswordReset if found.
     */
    Optional<PasswordReset> findByAccountIdAndOtp(Long accountId, String otp);
    
    /**
     * Finds the latest PasswordReset by account ID.
     *
     * @param accountId The account ID.
     * @return An Optional containing the latest PasswordReset if found.
     */
    Optional<PasswordReset> findTopByAccountIdOrderByExpirationTimeDesc(Long accountId);
}
