package com.example.user.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userRegisteredTopic() {
        return TopicBuilder.name("user-registered")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic otpVerificationTopic(){
        return TopicBuilder.name("otp-verification")
                .partitions(2)
                .replicas(1)
                .build();
    }
}