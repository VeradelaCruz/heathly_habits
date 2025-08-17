package com.example.habit_service.client;

import com.example.habit_service.dtos.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
//-----
public class UserClientService {

    private final WebClient webClient;

    public UserClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8083")
                .build();
    }

    public Mono<UserDTO> findUserById(String userId) {
        return webClient.get()
                .uri("/user/id/{id}", userId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .doOnNext(user -> System.out.println("User found: " + user))
                .doOnError(e -> System.err.println("Error trying to find the user: " + e.getMessage()))
                .onErrorResume(e -> Mono.empty());
    }


}

