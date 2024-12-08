package com.example.iotbackend.service.impl;

import com.example.iotbackend.model.PasswordReset;
import com.example.iotbackend.repository.PasswordResetRepository;
import com.example.iotbackend.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * PasswordResetServiceImpl provides the implementation of PasswordResetService,
 * handling operations related to PasswordReset entities.
 */
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetRepository passwordResetRepository;

    /**
     * Saves the given PasswordReset entity to the database.
     *
     * @param entity The PasswordReset entity to be saved.
     * @return The saved PasswordReset entity.
     */
    @Override
    public PasswordReset save(PasswordReset entity) {
        return passwordResetRepository.save(entity);
    }

    /**
     * Deletes a PasswordReset entity by its ID.
     *
     * @param id The ID of the PasswordReset entity to be deleted.
     */
    @Override
    public void delete(Long id) {
        passwordResetRepository.deleteById(id);
    }

    /**
     * Retrieves all PasswordReset entities from the database.
     *
     * @return A list of all PasswordReset entities.
     */
    @Override
    public List<PasswordReset> findAll() {
        return passwordResetRepository.findAll();
    }

    /**
     * Retrieves a PasswordReset entity by its ID.
     *
     * @param id The ID of the PasswordReset entity to retrieve.
     * @return An Optional containing the PasswordReset entity if found, or empty otherwise.
     */
    @Override
    public Optional<PasswordReset> findById(Long id) {
        return passwordResetRepository.findById(id);
    }

    /**
     * Retrieves the most recent PasswordReset entry for a given account ID.
     *
     * @param accountId The ID of the account.
     * @return An Optional containing the latest PasswordReset if found, or empty otherwise.
     */
    @Override
    public Optional<PasswordReset> findTopByAccountIdOrderByExpirationTimeDesc(Long accountId) {
        return passwordResetRepository.findTopByAccountIdOrderByExpirationTimeDesc(accountId);
    }

    /**
     * Retrieves a PasswordReset entry by account ID and OTP.
     *
     * @param accountId The ID of the account.
     * @param otp The One-Time Password to verify.
     * @return An Optional containing the PasswordReset if found, or empty otherwise.
     */
    @Override
    public Optional<PasswordReset> findByAccountIdAndOtp(Long accountId, String otp) {
        return passwordResetRepository.findByAccountIdAndOtp(accountId, otp);
    }
}
