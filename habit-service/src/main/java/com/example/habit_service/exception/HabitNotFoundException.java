package com.example.habit_service.exception;

public class HabitNotFoundException extends RuntimeException{
    public HabitNotFoundException(String id) {
        super("Habit with id " + id + " not found.");
    }
}
