package com.example.user_service.exception;

public class UserNotFoundByNameException extends RuntimeException {
    public UserNotFoundByNameException(String userName) {
        super("User with name " + userName + " not found.");
    }
}

