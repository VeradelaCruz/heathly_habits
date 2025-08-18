package com.example.habit_service.listener;

import com.example.habit_service.models.Habit;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

// 🏷 HabitConsumer
// Qué es: Componente de Spring (@Component) que se encarga de recibir mensajes de Kafka.
// Qué hace: Escucha de manera reactiva los mensajes del topic "habit-created" y procesa cada Habit recibido.
@Component
public class HabitConsumer {

    // 🔹 Constructor donde inyectamos el listener reactivo (Flux de ReceiverRecord)
    public HabitConsumer(Flux<ReceiverRecord<String, Habit>> kafkaListener) {

        // 🔹 Subscribimos al flujo de mensajes de Kafka
        kafkaListener
                .doOnNext(record -> {
                    // 📩 Obtenemos el objeto Habit del mensaje recibido
                    Habit habit = record.value();
                    System.out.println("📩 Habit recibido: " + habit);

                    // ✅ Confirmamos que el mensaje fue procesado (acknowledge)
                    record.receiverOffset().acknowledge();
                })
                .subscribe(); // 🔹 Necesario para activar la suscripción y procesar los mensajes
    }
}
