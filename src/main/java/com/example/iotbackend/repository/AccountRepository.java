package com.example.iotbackend.repository;

import com.example.iotbackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AccountRepository is responsible for performing database operations related to the Account entity.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds an Account by its email address.
     *
     * @param email - The email of the account to search for.
     * @return Optional<Account> - An optional containing the Account if found, or empty if not.
     */
    Optional<Account> findByEmail(String email);

}
