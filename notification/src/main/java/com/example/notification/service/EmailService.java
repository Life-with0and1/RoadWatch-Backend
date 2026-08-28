package com.example.notification.service;

import com.example.notification.event.OtpVerificationEvent;
import com.example.notification.event.UserRegisterEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class EmailService {

    private final RestClient restClient = RestClient.builder().baseUrl("https://api.resend.com").build();

    private final String apiKey = System.getenv("RESEND_API_KEY");

    public void sendOtpVerificationEmail(OtpVerificationEvent event) {

        Map<String, Object> body = Map.of(
                "from", "onboarding@resend.dev",
                "to", new String[]{event.getEmail()},
                "subject", "Verify your RoadWatch account",
                "html",
                "<p>Hello,</p>" +
                "<p>Your verification OTP is: <strong>" + event.getOtp() + "</strong></p>" +
                "<p>This OTP is valid for 15 minutes.</p>"
        );

        restClient.post()
                .uri("/emails")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    public void sendWelcomeEmail(UserRegisterEvent event) {

        Map<String, Object> body = Map.of(
                "from", "onboarding@resend.dev",
                "to", new String[]{event.getEmail()},
                "subject", "Welcome to RoadWatch",
                "html", "<p>Hi " + event.getName() + ",</p>" +
                        "<p>Your account has been created successfully.</p>"
        );

        restClient.post()
                .uri("/emails")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}