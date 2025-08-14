package com.example.habit_service.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("User with id + " + id + " not found." );
    }
}