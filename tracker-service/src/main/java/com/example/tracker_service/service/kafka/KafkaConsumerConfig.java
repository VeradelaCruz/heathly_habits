package com.example.tracker_service.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {
    // 🛠 Bean principal que configura al consumidor de Kafka:
    // Le indica al broker qué topic escuchar, en qué grupo de consumidores está,
    // cómo deserializar las claves y valores, y desde qué offset empezar.
    @Bean
    public ReceiverOptions<String, String> receiverOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092"); // 🌐 Dirección del broker
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "tracker-group"); // 👥 Grupo de consumidores
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // 🔑 Cómo leer la key
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // 📩 Cómo leer el valor
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // ⏪ Desde el primer mensaje si no hay offset guardado

        return ReceiverOptions.<String, String>create(props)
                .subscription(Collections.singleton("tracker-created")); // 📝 Topic a escuchar
    }

    // 🔔 Bean que crea un listener reactivo de Kafka
    // Convierte los mensajes en un Flux (stream reactivo)
    // doOnNext se ejecuta cada vez que llega un mensaje
    @Bean
    public Flux<ReceiverRecord<String, String>> kafkaListener(ReceiverOptions<String, String> options) {
        return KafkaReceiver.create(options)
                .receive()
                .doOnNext(record -> {
                    System.out.println("📩 Mensaje recibido: " + record.value()); // 🖨 Log básico
                });
    }

    // 🛡 Bean similar al anterior, pero con "interceptor"
    // Sirve para hacer validaciones, logging avanzado, métricas, etc.
    // doOnError captura errores al recibir mensajes
    @Bean
    public Flux<ReceiverRecord<String, String>> kafkaListenerWithInterceptor(ReceiverOptions<String, String> options) {
        return KafkaReceiver.create(options)
                .receive()
                .doOnNext(record -> {
                    log.info("🕵️‍♂️ Interceptor: mensaje recibido con key {}", record.key());
                })
                .doOnError(ex -> log.error("⚠️ Error al recibir mensaje", ex));
    }
}
