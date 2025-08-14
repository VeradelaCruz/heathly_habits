package com.example.tracker_service.client;

import com.example.tracker_service.dtos.HabitDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class HabitServiceClient {

    private final WebClient webClient;

    public HabitServiceClient(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8081")
                .build();
    }

    public Mono<HabitDTO> findHabitById(String habitId){
        return webClient.get()
                .uri("/habits/{id}", habitId)
                .retrieve()
                .bodyToMono(HabitDTO.class)
                .doOnNext(user -> System.out.println("Habit found: " + user))
                .doOnError(e -> System.err.println("Error trying to find the habit: " + e.getMessage()))
                .onErrorResume(e -> Mono.empty());
    }

}