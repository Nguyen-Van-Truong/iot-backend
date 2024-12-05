package com.example.iotbackend.service.impl;

import com.example.iotbackend.dto.response.AccountResponse;
import com.example.iotbackend.exception.BadRequestException;
import com.example.iotbackend.exception.ResourceNotFoundException;
import com.example.iotbackend.model.Account;
import com.example.iotbackend.repository.AccountRepository;
import com.example.iotbackend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends AbstractService<Account, Long> implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected JpaRepository<Account, Long> getRepository() {
        return accountRepository;
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = getRepository().findAll();
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found in the database.");
        }
        return accounts;
    }

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

    @Override
    public Account save(Account account) {
        if (account.getEmail() == null || account.getEmail().isEmpty()) {
            throw new BadRequestException("Email must not be blank");
        }
        if (account.getPassword() == null || account.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters long");
        }
        if (account.getFullName() == null || account.getFullName().isEmpty()) {
            throw new BadRequestException("Full name must not be blank");
        }

        if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already in use");
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        return getRepository().save(account);
    }

}
