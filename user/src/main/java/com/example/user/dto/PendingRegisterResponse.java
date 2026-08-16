package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PendingRegisterResponse {
    private Long id;
    private String email;
    private String message;
}
