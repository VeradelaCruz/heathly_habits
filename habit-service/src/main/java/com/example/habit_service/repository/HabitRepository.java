package com.example.habit_service.repository;

import com.example.habit_service.models.Habit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface HabitRepository extends ReactiveMongoRepository<Habit, String> {
    Flux<Habit> findByUserId(String userId);

    Flux<Habit> findByCategory(String category);

}
