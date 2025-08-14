package com.example.user_service.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super("This user already exists. ");
    }
}
