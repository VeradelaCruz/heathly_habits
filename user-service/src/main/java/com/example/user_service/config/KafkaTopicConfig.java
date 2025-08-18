package com.example.user_service.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import javax.swing.text.AbstractDocument;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    public KafkaAdmin kafkaAdmin(){
        Map<String,Object> configs= new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic userCreatedTopic(){
        return new NewTopic("user-created", 3, (short) 1);
    }
}
