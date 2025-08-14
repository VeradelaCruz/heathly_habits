package com.example.tracker_service.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("User with id "+ userId + " not found.");
    }
}
