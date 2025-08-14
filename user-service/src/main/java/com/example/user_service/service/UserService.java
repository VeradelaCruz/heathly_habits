package com.example.user_service.service;

import com.example.user_service.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> createUser(User user);
    Flux<User> getAllUsers();
    Mono<User> getUserById(String id);
    Mono<Void> deleteUser(String id);
    Mono<User> getUserByName(String userName);
}
