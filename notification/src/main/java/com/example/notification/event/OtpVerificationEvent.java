package com.example.notification.event;

import lombok.Getter;

@Getter
public class OtpVerificationEvent {
    private String email;
    private String otp;
}
