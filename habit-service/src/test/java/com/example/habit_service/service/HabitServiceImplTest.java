package com.example.habit_service.service;


import com.example.habit_service.client.UserClientService;
import com.example.habit_service.dtos.HabitWithUserDTO;
import com.example.habit_service.dtos.UserDTO;
import com.example.habit_service.enums.Frequency;
import com.example.habit_service.exception.CategoryNotFoundException;
import com.example.habit_service.exception.HabitNotFoundException;
import com.example.habit_service.exception.UserNotFoundException;
import com.example.habit_service.models.Habit;
import com.example.habit_service.repository.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class HabitServiceImplTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private UserClientService userClientService;

    @InjectMocks
    private HabitServiceImpl habitService;

    private Habit habit1;
    private List<Habit> habitList;
    private UserDTO user;
    private HabitWithUserDTO habitWithUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        habit1 = new Habit();
        habit1.setId("1");
        habit1.setName("Hacer ejercicio");
        habit1.setUserId("u1");
        habit1.setCategory("Health");
        habit1.setDescription("---");
        habit1.setFrequency(Frequency.SOMETIMES);

        user = new UserDTO();
        user.setId("u1");
        user.setUserName("name1");

        habitWithUserDTO= new HabitWithUserDTO();
        habitWithUserDTO.setHabit(habit1);
        habitWithUserDTO.setUserDTO(user);

        // Creamos varios hábitos para la lista
        Habit habit2 = new Habit();
        habit2.setId("2");
        habit2.setName("Leer un libro");
        habit2.setUserId("u2");
        habit2.setCategory("Health");

        Habit habit3 = new Habit();
        habit3.setId("3");
        habit3.setName("Dormir temprano");
        habit3.setUserId("u1");
        habit3.setCategory("Health");

        habitList = List.of(habit1, habit2, habit3);
    }


    @Test
    void getHabitById_habitExists_returnsHabit() {
        // GIVEN
        when(habitRepository.findById("1")).thenReturn(Mono.just(habit1));

        // WHEN & THEN
        StepVerifier.create(habitService.getHabitById("1"))
                .expectNext(habit1)
                .verifyComplete();

        verify(habitRepository, times(1)).findById("1");
    }

    @Test
    void getHabitById_habitDoesNotExist_throwsException() {
        // GIVEN
        when(habitRepository.findById("1")).thenReturn(Mono.empty());

        // WHEN & THEN
        StepVerifier.create(habitService.getHabitById("1"))
                .expectErrorMatches(throwable ->
                        throwable instanceof HabitNotFoundException &&
                                throwable.getMessage().contains("1")
                )
                .verify();

        verify(habitRepository, times(1)).findById("1");
    }

    @Test
    void getAllHabits_returnsAllHabits() {
        // Mockeamos que el repositorio devuelve el flujo con la lista de hábitos
        when(habitRepository.findAll()).thenReturn(Flux.fromIterable(habitList));

        StepVerifier.create(habitService.getAllHabits())
                .expectNextSequence(habitList) // espera la secuencia completa
                .verifyComplete();

        verify(habitRepository, times(1)).findAll();
    }

    @Test
    void createHabit_returnsHabit(){
        //GIVEN
        when(habitRepository.save(habit1)).thenReturn(Mono.just(habit1));

        //WHEN & THEN
        StepVerifier.create(habitService.createHabit(habit1))
                .expectNext(habit1)
                .verifyComplete();

        verify(habitRepository, times(1)).save(habit1);
    }

    @Test
    void deleteHabit_returnsVoid(){
        //GIVEN
        when(habitRepository.findById(habit1.getId())).thenReturn(Mono.just(habit1));
        when(habitRepository.deleteById(habit1.getId())).thenReturn(Mono.empty());

        //WHEN & THEN
        StepVerifier.create(habitService.deleteHabit(habit1.getId()))
                .expectNextCount(0)
                .verifyComplete();

        verify(habitRepository,times(1)).deleteById(habit1.getId());
        verify(habitRepository, times(1)).findById(habit1.getId());
    }

    @Test
    void deleteHabit_habitDoesNotExist_throwsException(){
        //GIVEN
        when(habitRepository.findById(habit1.getId())).thenReturn(Mono.empty());


        //WHEN & THEN
        StepVerifier.create(habitService.deleteHabit(habit1.getId()))
                .expectErrorMatches(e ->
                        e instanceof HabitNotFoundException)
                .verify();

        verify(habitRepository,never()).deleteById(habit1.getId());
        verify(habitRepository, times(1)).findById(habit1.getId());

    }
    @Test
    void getHabitsByUserId_returnsHabits(){
        // Filtramos localmente la lista para mockear solo los hábitos del usuario
        List<Habit> filteredHabits = habitList.stream()
                .filter(h -> h.getUserId().equals(habit1.getUserId()))
                .toList();

        //GIVEN
        when(habitRepository.findByUserId(habit1.getUserId())).thenReturn(Flux.fromIterable(filteredHabits));

        //WHEN & THEN
        StepVerifier.create(habitRepository.findByUserId(habit1.getUserId()))
                .expectNextSequence(filteredHabits)
                .verifyComplete();

        verify(habitRepository, times(1)).findByUserId(habit1.getUserId());
    }

    @Test
    void getHabitsByUserId_userDoesNotExist_throwsException() {
        // GIVEN: el repositorio devuelve flujo vacío para ese userId
        when(habitRepository.findByUserId(habit1.getUserId())).thenReturn(Flux.empty());

        // WHEN & THEN: el servicio debe lanzar UserNotFoundException
        StepVerifier.create(habitService.getHabitsByUserId(habit1.getUserId()))
                .expectErrorMatches(e -> e instanceof UserNotFoundException &&
                        e.getMessage().contains(habit1.getUserId()))
                .verify();

        verify(habitRepository, times(1)).findByUserId(habit1.getUserId());
    }

    @Test
    void getHabitsByCategory_returnsHabits(){
        List<Habit> filterList = habitList.stream()
                .filter(h -> h.getCategory().equals(habit1.getCategory()))
                .toList();

        //GIVEN
        when(habitRepository.findByCategory(habit1.getCategory())).thenReturn(Flux.fromIterable(filterList));

        //WHEN & THEN
        StepVerifier.create(habitRepository.findByCategory(habit1.getCategory()))
                .expectNextSequence(filterList)
                .verifyComplete();

        verify(habitRepository, times(1)).findByCategory(habit1.getCategory());

    }
    @Test
    void getHabitsByCategory_categoryDoesNotExists_throwsException(){
        //GIVEN
        when(habitRepository.findByCategory(habit1.getCategory())).thenReturn(Flux.empty());

        //WHEN & THEN
        StepVerifier.create(habitService.getHabitsByCategory(habit1.getCategory()))
                .expectErrorMatches(e -> e instanceof CategoryNotFoundException &&
                        e.getMessage().contains(habit1.getCategory()));

        verify(habitRepository, times(1)).findByCategory(habit1.getCategory());

    }

    @Test
    void  getHabitWithUser_success(){
        when(habitRepository.findById("h1")).thenReturn(Mono.just(habit1));
        when(userClientService.findUserById("u1")).thenReturn(Mono.just(user));

        StepVerifier.create(habitService.getHabitWithUser("h1"))
                .expectNextMatches(hwu -> hwu.getHabit().equals(habit1) && hwu.getUserDTO().equals(user))
                .verifyComplete();

        verify(habitRepository, times(1)).findById("h1");
        verify(userClientService, times(1)).findUserById("u1");

    }

    @Test
    void getHabitWithUser_habitNotFound() {
        when(habitRepository.findById("h1")).thenReturn(Mono.empty());

        StepVerifier.create(habitService.getHabitWithUser("h1"))
                .expectError(HabitNotFoundException.class)
                .verify();

        verify(habitRepository, times(1)).findById("h1");
        verifyNoInteractions(userClientService);
    }

    @Test
    void getHabitWithUser_userNotFound() {
        Habit habit = new Habit();
        habit.setId("h1");
        habit.setUserId("u1");

        when(habitRepository.findById("h1")).thenReturn(Mono.just(habit));
        when(userClientService.findUserById("u1")).thenReturn(Mono.empty());

        StepVerifier.create(habitService.getHabitWithUser("h1"))
                .expectError(UserNotFoundException.class)
                .verify();

        verify(habitRepository, times(1)).findById("h1");
        verify(userClientService, times(1)).findUserById("u1");
    }
}
