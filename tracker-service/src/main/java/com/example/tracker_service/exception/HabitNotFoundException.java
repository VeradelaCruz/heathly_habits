package com.example.tracker_service.exception;

public class HabitNotFoundException extends RuntimeException {
    public HabitNotFoundException(String habitId) {
        super("Habit with id "+ habitId + "not found.");
    }
}