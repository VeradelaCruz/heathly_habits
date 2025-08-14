package com.example.habit_service.exception;


public class DuplicateHabitException extends RuntimeException {
    public DuplicateHabitException(String message) {
        super(message);
    }
}
