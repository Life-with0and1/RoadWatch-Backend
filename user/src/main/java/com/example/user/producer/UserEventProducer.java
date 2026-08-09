package com.example.user.producer;

import com.example.user.event.OtpVerificationEvent;
import com.example.user.event.UserRegisterEvent;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private final KafkaTemplate<String, UserRegisterEvent> userRegisterKafkaTemplate;
    private final KafkaTemplate<String, OtpVerificationEvent> otpVerificationKafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserRegisterEvent> userRegisterKafkaTemplate,KafkaTemplate<String, OtpVerificationEvent> otpVerificationKafkaTemplate){
        this.userRegisterKafkaTemplate = userRegisterKafkaTemplate;
        this.otpVerificationKafkaTemplate = otpVerificationKafkaTemplate;
    }

    public void publishUserRegister(UserRegisterEvent event){
        userRegisterKafkaTemplate.send("user-registered", event);
    }

    public void publishOtpSent(OtpVerificationEvent event){
        otpVerificationKafkaTemplate.send("otp-verification", event);
    }
}
