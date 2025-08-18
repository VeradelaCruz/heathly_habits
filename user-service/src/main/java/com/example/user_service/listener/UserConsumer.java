package com.example.user_service.listener;

import com.example.user_service.models.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

@Component
public class UserConsumer {
    // 🔹 Constructor donde inyectamos el listener reactivo
    public UserConsumer(Flux<ReceiverRecord<String, User>> kafkaListener) {

        // 🔹 Subscribimos al flujo de mensajes de Kafka
        kafkaListener
                .doOnNext(record -> {
                    // 📩 Obtenemos el objeto User del mensaje recibido
                    User user = record.value();
                    System.out.println("📩 User recibido: " + user);

                    // ✅ Confirmamos que el mensaje fue procesado (acknowledge)
                    record.receiverOffset().acknowledge();
                })
                .subscribe(); // 🔹 Necesario para activar la suscripción y procesar los mensajes
    }
}
