package com.example.tracker_service.repository;

import com.example.tracker_service.models.Tracker;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface TrackerRepository extends ReactiveMongoRepository<Tracker, String> {

    Flux<Tracker> findTrackersByUserId(String userId);
    Flux<Tracker> findTrackersByHabitId(String habitId);
    Mono<Tracker> findByUserIdAndHabitIdAndDate(String userId, String habitId, LocalDate date);


}
