package com.example.iotbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * AccountResponse is a DTO used to return account details.
 * It includes account-related information such as ID, email, full name, phone number, role, and timestamps.
 */
@Getter
@Setter
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Long roleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
