package com.example.iotbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
