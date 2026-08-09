package com.example.user.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OtpVerificationEvent {
    
    private String email;
    private String otp;

}
