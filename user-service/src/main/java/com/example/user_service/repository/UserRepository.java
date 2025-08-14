package com.example.user_service.repository;

import com.example.user_service.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findUserByUserName(String userName);

    Mono<User >findUserByUserNameAndEmail (String userName, String email);

}
