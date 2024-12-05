package com.example.iotbackend.service;

import com.example.iotbackend.dto.response.AccountResponse;
import com.example.iotbackend.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService extends IService<Account, Long> {
    Optional<Account> findByEmail(String email);
    AccountResponse getAccountResponseById(Long id);
}
