package com.example.user_service.controller;

import com.example.user_service.exception.DuplicateUserException;
import com.example.user_service.models.User;
import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/addUser")
    public Mono<ResponseEntity<Object>> addUser(@Valid @RequestBody User user) {
        return userService.createUser(user)
                .map(userCreated -> ResponseEntity.status(HttpStatus.CREATED).body((Object) userCreated))
                .onErrorResume(DuplicateUserException.class, ex ->
                        Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body((Object) ex.getMessage()))
                )
                .onErrorResume(ex ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body((Object) ("Unexpected error: " + ex.getMessage()))));
    }

    @GetMapping("/getAll")
    public Flux<User> findAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/id/{id}")
    public Mono<ResponseEntity<?>> findUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<?>> removeUser(@PathVariable String id) {
        return userService.getUserById(id)
                .thenReturn(ResponseEntity.ok(
                        Map.of("message", "User with id " + id + " was deleted successfully.")));
    }

    @GetMapping("/byName/{userName}")
    public Mono<ResponseEntity<?>> findUserByName(@PathVariable String userName) {
        return userService.getUserByName(userName)
                .map(foundUser -> ResponseEntity.ok().body(foundUser));
    }
}
