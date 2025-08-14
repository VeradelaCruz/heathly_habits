package com.example.tracker_service.service;

import com.example.tracker_service.client.HabitServiceClient;
import com.example.tracker_service.client.UserServiceClient;
import com.example.tracker_service.dtos.TrackerWithUserAndHabit;
import com.example.tracker_service.exception.DuplicateTrackerException;
import com.example.tracker_service.exception.HabitNotFoundException;
import com.example.tracker_service.exception.TrackerNotFoundException;
import com.example.tracker_service.exception.UserNotFoundException;
import com.example.tracker_service.models.Tracker;
import com.example.tracker_service.repository.TrackerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TrackerServiceImpl implements TrackerService{

    private final TrackerRepository trackerRepository;
    private final UserServiceClient userServiceClient;
    private final HabitServiceClient habitServiceClient;

    @Override
    public Mono<Tracker> createTracker(Tracker tracker) {
        return trackerRepository.findByUserIdAndHabitIdAndDate(
                        tracker.getUserId(),
                        tracker.getHabitId(),
                        tracker.getDate()
                )
                .flatMap(existing -> Mono.<Tracker>error(new DuplicateTrackerException("Tracker already exists")))
                .switchIfEmpty(Mono.defer(() -> trackerRepository.save(tracker)));
    }


    @Override
    public Flux<Tracker> getAllTrackers() {
        return trackerRepository.findAll();
    }

    @Override

    public Mono<Tracker> getTrackerById(String id) {
        return trackerRepository.findById(id)
                .switchIfEmpty(Mono.error(new TrackerNotFoundException(id)));
    }

    @Override
    public Mono<Void> deleteTracker(String id) {
        return trackerRepository.findById(id)
                .switchIfEmpty(Mono.error(new TrackerNotFoundException(id)))
                .flatMap(existingTracker -> trackerRepository.deleteById(id));
    }

    @Override
    public Flux<Tracker> getTrackersByUserId(String userId) {
        return trackerRepository.findTrackersByUserId(userId)
                .collectList() // 🚨 Espera a que el Flux termine y lo convierte en una List<Tracker>
                .flatMapMany(list -> { // 🔁 Luego convierte la lista en un nuevo Flux
                    if (list.isEmpty()) {
                        return Flux.error(new UserNotFoundException(userId)); // ❌ Si está vacía, lanza error
                    }
                    return Flux.fromIterable(list); // ✅ Si hay elementos, los vuelve a emitir como Flux
                });
    }


    @Override
    public Flux<Tracker> getTrackersByHabitId(String habitId) {
        return trackerRepository.findTrackersByHabitId(habitId)
                .collectList()
                .flatMapMany(list->{
                    if (list.isEmpty()){
                        return Flux.error(new HabitNotFoundException(habitId));
                    }
                    return Flux.fromIterable(list);
                });
    }

    @Override
    public Mono<TrackerWithUserAndHabit> findTrackerWithUserAndHabit(String id){
        return trackerRepository.findById(id)
                .switchIfEmpty(Mono.error(new TrackerNotFoundException(id)))
                .flatMap(tracker -> Mono.zip(
                        userServiceClient.findUserById(tracker.getUserId())
                                .switchIfEmpty(Mono.error(new UserNotFoundException(tracker.getUserId()))),
                        habitServiceClient.findHabitById(tracker.getHabitId())
                                .switchIfEmpty(Mono.error(new HabitNotFoundException(tracker.getHabitId()))),
                        (user, habit) -> new TrackerWithUserAndHabit(tracker, user, habit)
                ));
    }
}