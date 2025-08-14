package com.example.habit_service.service;

import com.example.habit_service.dtos.HabitWithUserDTO;
import com.example.habit_service.models.Habit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HabitService {
    Mono<Habit> createHabit(Habit habit);
    Flux<Habit> getAllHabits();
    Mono<Habit> getHabitById(String id);
    Mono<Void> deleteHabit(String id);

    Flux<Habit> getHabitsByUserId(String userId);
    Flux<Habit> getHabitsByCategory(String category);

    Mono<HabitWithUserDTO> getHabitWithUser(String habitId);
}
