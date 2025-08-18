package com.example.habit_service.config;


import com.example.habit_service.models.Habit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


// 🏷 HabitProducer
// Qué es: Clase de servicio (@Service) que se encarga de enviar mensajes a Kafka.
// Qué hace: Usa KafkaTemplate (definido en HabitProducerConfig) para publicar objetos Habit en el topic "habit-created".
@Service
@RequiredArgsConstructor // 🔹 Genera el constructor automáticamente para inyectar dependencias finales
@Slf4j // 🔹 Permite usar log.info() y log.error() para logging
public class HabitProducer {

    // 🔹 KafkaTemplate que permite enviar mensajes de Habit a Kafka
    private final KafkaTemplate<String, Habit> kafkaTemplate;

    // 🔹 Nombre del topic donde se publicarán los mensajes
    private static final String TOPIC = "habit-created";

    // 🔹 Método reactivo que envía un mensaje de tipo Habit a Kafka
    public Mono<SendResult<String, Habit>> sendHabit(Habit habit) {
        return Mono.create(sink -> {
            // 🔹 Enviamos el objeto Habit al topic usando KafkaTemplate
            kafkaTemplate.send(TOPIC, habit)
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




