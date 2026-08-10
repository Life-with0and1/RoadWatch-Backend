package com.example.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class VerificationDTO {
    @Pattern(regexp = "\\d{4}",message = "OTP must be exactly 4 digits")
    private String otp;

    @NotNull(message = "User id is required")
    private Long id;
}
