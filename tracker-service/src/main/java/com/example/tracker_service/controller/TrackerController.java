package com.example.tracker_service.controller;

import com.example.tracker_service.dtos.TrackerWithUserAndHabit;
import com.example.tracker_service.models.Tracker;
import com.example.tracker_service.service.TrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tracker")
public class TrackerController {

    private final TrackerService trackerService;

    @PostMapping("/addTracker")
    public Mono<ResponseEntity<Tracker>> addTracker(@Valid @RequestBody Tracker tracker) {
        return trackerService.createTracker(tracker)
                .map(trackerSaved -> ResponseEntity.status(HttpStatus.CREATED).body(trackerSaved));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Tracker>> findById(@PathVariable String id) {
        return trackerService.getTrackerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/getAll")
    public Flux<Tracker> findAllTrackers() {
        return trackerService.getAllTrackers();
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> removeTracker(@PathVariable String id) {
        return trackerService.getTrackerById(id)
                .flatMap(tracker -> trackerService.deleteTracker(id)
                        .thenReturn(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/byUserId/{userId}")
    public Flux<Tracker> findByUserId(@PathVariable String userId) {
        return trackerService.getTrackersByUserId(userId);
    }

    @GetMapping("/byHabitId/{habitId}")
    public Flux<Tracker> findByHabitId(@PathVariable String habitId) {
        return trackerService.getTrackersByHabitId(habitId);
    }

    @GetMapping("/with-user-habits/{id}")
    public Mono<ResponseEntity<TrackerWithUserAndHabit>> getTrackerWithDetails(@PathVariable String id) {
        return trackerService.findTrackerWithUserAndHabit(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
