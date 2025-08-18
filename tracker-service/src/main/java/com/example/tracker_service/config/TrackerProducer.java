package com.example.tracker_service.config;

import com.example.tracker_service.models.Tracker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackerProducer {

    // 🔹 KafkaTemplate que permite enviar mensajes de Habit a Kafka
    private final KafkaTemplate<String, Tracker> kafkaTemplate;

    // 🔹 Nombre del topic donde se publicarán los mensajes
    private static final String TOPIC = "tracker-created";

    // 🔹 Metodo reactivo que envía un mensaje de tipo Habit a Kafka
    public Mono<SendResult<String,Tracker>> sendTracker (Tracker tracker) {
        return Mono.create(sink -> {
            kafkaTemplate.send(TOPIC, tracker)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            // ✅ Si no hay error, hacemos log del offset y completamos el Mono con éxito
                            log.info("✅ Mensaje enviado con offset {}", result.getRecordMetadata().offset());
                            sink.success(result);
                        } else {
                            // ❌ Si ocurre un error, hacemos log y emitimos error en el Mono
                            log.error("❌ Error enviando mensaje", ex);
                            sink.error(ex);
                        }
                    });
        });
    }
}
