package com.example.tracker_service.exception;

public class DuplicateTrackerException extends RuntimeException {
    public DuplicateTrackerException(String message) {
        super("Tracker already exists");
    }
}
