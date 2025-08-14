package com.example.tracker_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

public class GlobalHandlerException {
    @ExceptionHandler(DuplicateTrackerException.class)
    public ResponseEntity<String> handleDuplicateHabit(DuplicateTrackerException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)  // para cualquier otra excepción no manejada
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error: " + ex.getMessage());
    }

    @ExceptionHandler(TrackerNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleTrackerNotFoundException(TrackerNotFoundException ex) {
        Map<String, String> error = Map.of("message", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleUserNotFoundException(TrackerNotFoundException ex) {
        Map<String, String> error = Map.of("message", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(HabitNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleHabitNotFoundException(TrackerNotFoundException ex) {
        Map<String, String> error = Map.of("message", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }
}
