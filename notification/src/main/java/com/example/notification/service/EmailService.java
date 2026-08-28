package com.example.notification.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.notification.event.OtpVerificationEvent;
import com.example.notification.event.UserRegisterEvent;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(UserRegisterEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("ayushkumarit01@gmail.com");
        message.setTo(event.getEmail());

        message.setSubject("Welcome to Backend Mastery");

        message.setText("Hi " + event.getName() + ",\n\n" + "Your account has been created successfully.\n\n" + "Welcome!");

        mailSender.send(message);
    }

    public void sendOtpVerificationEmail(OtpVerificationEvent event) {
        System.out.println("OTP EMAIL EVENT RECEIVED: " + event.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("ayushkumarit01@gmail.com");
        message.setTo(event.getEmail());

        message.setSubject("Verify your RoadWatch account");

        message.setText(
                "Hello,\n\n" +
                "Thank you for registering with RoadWatch.\n\n" +
                "Your verification OTP is: " + event.getOtp() + "\n\n" +
                "This OTP is valid for 15 minutes.\n\n" +
                "If you did not create this account, you can safely ignore this email.\n\n" +
                "Regards,\n" +
                "RoadWatch Team"
        );

       System.out.println("BEFORE MAIL SEND");

        mailSender.send(message);

        System.out.println("AFTER MAIL SEND");
    }

}