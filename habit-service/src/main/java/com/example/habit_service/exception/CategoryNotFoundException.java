package com.example.habit_service.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String category) {
        super("This category could not be found.");
    }
}
