package com.example.notification.consumer;

import com.example.notification.event.OtpVerificationEvent;
import com.example.notification.event.UserRegisterEvent;
import com.example.notification.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventConsumer {

    private final EmailService emailService;

    public UserEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }


    @KafkaListener(topics = "user-registered", groupId = "notification-group-v2")
    public void consumeUserRegister(UserRegisterEvent event) {
        emailService.sendWelcomeEmail(event);
    }

    @KafkaListener(topics = "otp-verification", groupId = "notification-group-v2")
    public void consumeOtpVerification(OtpVerificationEvent event){
        emailService.sendOtpVerificationEmail(event);
    }
}