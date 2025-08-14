package com.example.tracker_service.service;

import com.example.tracker_service.dtos.TrackerWithUserAndHabit;
import com.example.tracker_service.models.Tracker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TrackerService {
    Mono<Tracker> createTracker(Tracker tracker);
    Flux<Tracker> getAllTrackers();
    Mono<Tracker> getTrackerById(String id);
    Mono<Void> deleteTracker(String id);
    Flux<Tracker> getTrackersByUserId(String userId);
    Flux<Tracker> getTrackersByHabitId(String habitId);

    Mono<TrackerWithUserAndHabit> findTrackerWithUserAndHabit(String id);
}
