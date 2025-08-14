package com.example.habit_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateHabitException.class)
    public ResponseEntity<String> handleDuplicateHabit(DuplicateHabitException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)  // para cualquier otra excepción no manejada
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error: " + ex.getMessage());
    }
    @ExceptionHandler(HabitNotFoundException.class)
    Mono<ResponseEntity<Map<String, String>>> handleHabitNotFoundByNameException(HabitNotFoundException ex) {
        Map<String, String> error = Map.of("message", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(UserNotFoundException.class)
    Mono<ResponseEntity<Map<String, String>>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> error = Map.of("message", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    Mono<ResponseEntity<Map<String, String>>>handleCategoryNotFoundException( CategoryNotFoundException ex){
        Map<String, String> error = Map.of("message", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }
}
