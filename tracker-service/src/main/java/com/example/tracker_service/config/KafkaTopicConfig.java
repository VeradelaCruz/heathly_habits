package com.example.tracker_service.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class KafkaTopicConfig {

    //Administra kafka al iniciar la aplicacion
    public KafkaAdmin kafkaAdmin(){
        Map<String, Object> configs= new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"kafka:9092"); //Direccion del broker.
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic trackerCreatedTopic(){
        return  new NewTopic("tracker-created", 3, (short) 1);
    }
}
