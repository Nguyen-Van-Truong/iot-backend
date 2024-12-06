package com.example.iotbackend.controller;

import com.example.iotbackend.dto.response.AccountResponse;
import com.example.iotbackend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * AccountController is responsible for handling requests related to user accounts.
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Endpoint to fetch account details by account ID.
     *
     * @param id - The ID of the account to be fetched.
     * @return AccountResponse - A response containing account details for the requested ID.
     */
    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable Long id) {
        return accountService.getAccountResponseById(id);  // Retrieve account details from the service
    }
}
