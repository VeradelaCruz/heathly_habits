package com.example.habit_service.service;


import com.example.habit_service.client.UserClientService;
import com.example.habit_service.config.HabitProducer;
import com.example.habit_service.dtos.HabitWithUserDTO;
import com.example.habit_service.exception.CategoryNotFoundException;
import com.example.habit_service.exception.HabitNotFoundException;
import com.example.habit_service.exception.UserNotFoundException;
import com.example.habit_service.models.Habit;
import com.example.habit_service.repository.HabitRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class HabitServiceImpl implements HabitService{

    private final HabitRepository habitRepository;
    private final UserClientService userClientService;
    private final KafkaTemplate<String, Habit> kafkaTemplate;


    @Override
    public Mono<Habit> createHabit(Habit habit) {
        return habitRepository.save(habit)
                .doOnSuccess(savedHabit -> kafkaTemplate.send("habits-topic", savedHabit));
    }


    @Override
    public Flux<Habit> getAllHabits() {
        return habitRepository.findAll();
    }

    @Override
    public Mono<Habit> getHabitById(String id) {
        return habitRepository.findById(id)
                .switchIfEmpty(Mono.error(new HabitNotFoundException(id)));
    }

    @Override
    public Mono<Void> deleteHabit(String id) {
        return habitRepository.findById(id)
                .switchIfEmpty(Mono.error(new HabitNotFoundException(id)))
                .flatMap(habit -> habitRepository.deleteById(id));
    }


    @Override
    public Flux<Habit> getHabitsByUserId(String userId) {
        return habitRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException(userId)));
    }

    @Override
    public Flux<Habit> getHabitsByCategory(String category) {
        return habitRepository.findByCategory(category)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException(category)));
    }

    public Mono<HabitWithUserDTO> getHabitWithUser(String habitId) {
        return habitRepository.findById(habitId)
                .switchIfEmpty(Mono.error(new HabitNotFoundException(habitId)))
                .flatMap(habit ->
                        userClientService.findUserById(habit.getUserId())
                                .switchIfEmpty(Mono.error(new UserNotFoundException(habit.getUserId())))
                                .map(user -> new HabitWithUserDTO(habit, user))
                );
    }


}