package com.example.habit_service.config;

import com.example.habit_service.models.Habit;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


//KafkaProducerConfig
//Qué es: Clase de configuración (@Configuration) que define cómo se comunica tu aplicación con Kafka.
//Qué hace: Crea los beans de ProducerFactory y KafkaTemplate que luego vas a usar para enviar mensajes.
//No envía mensajes por sí misma, solo prepara el “canal” para enviarlos.
@Configuration
public class HabitProducerConfig {

    // 📌 Leemos la URL del broker Kafka desde application.properties
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // 🏭 Bean que crea el ProducerFactory
    // ProducerFactory se encarga de construir los productores de Kafka con la configuración indicada
    @Bean
    public ProducerFactory<String, Habit> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // 🔹 Dirección del broker Kafka al que se enviarán los mensajes
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 🔹 Serializador de la clave del mensaje (String en este caso)
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 🔹 Serializador del valor del mensaje (en este caso el objeto Habit se convierte a JSON)
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // 🔹 Creamos y retornamos la fábrica de productores
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // 🚀 Bean que expone el KafkaTemplate
    // KafkaTemplate es el helper principal para enviar mensajes a Kafka de forma sencilla
    @Bean
    public KafkaTemplate<String, Habit> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
