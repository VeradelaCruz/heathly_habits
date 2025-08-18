package com.example.tracker_service.listener;

import com.example.tracker_service.models.Tracker;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

@Component
public class TrackerConsumer {

    // 🔹 Constructor donde inyectamos el listener reactivo
    public TrackerConsumer(Flux<ReceiverRecord<String, Tracker>> kafkaListener) {
        kafkaListener.
                doOnNext(record->{
                    Tracker tracker= record.value();
                    System.out.println("📩 Tracker recibido: " + tracker);
                    record.receiverOffset().acknowledge();
                })
                .subscribe();
    }
}
