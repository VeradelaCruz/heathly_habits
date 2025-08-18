package com.example.habit_service.config;


import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

// 🏷 KafkaTopicConfig (o KafkaAdminConfig)
// Qué es: Clase de configuración (@Configuration) que administra la infraestructura de Kafka.
// Qué hace: Crea automáticamente topics, define particiones y réplicas si no existen.
// Nota: No envía mensajes, solo asegura que los topics estén listos para usarse.
@Configuration
public class KafkaTopicConfig {

    // 💡 KafkaAdmin se encarga de la administración de Kafka al iniciar la aplicación.
    //     Si el topic no existe, lo crea automáticamente.
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092"); // 🔹 Dirección del broker
        return new KafkaAdmin(configs);
    }

    // ✅ Este bean crea el topic "habit-created" al iniciar la app.
    //    Define 3 particiones y 1 réplica (puedes ajustarlo según necesidades).
    @Bean
    public NewTopic habitCreatedTopic() {
        return new NewTopic("habit-created", 3, (short) 1);
    }
}
