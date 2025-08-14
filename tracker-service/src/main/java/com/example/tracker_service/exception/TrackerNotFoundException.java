package com.example.tracker_service.exception;

public class TrackerNotFoundException extends RuntimeException {
    public TrackerNotFoundException(String id) {
        super("Tracker with id " + id + " not found.");
    }
}