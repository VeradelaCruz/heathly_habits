package com.example.habit_service.service;


import com.example.habit_service.client.UserClientService;
import com.example.habit_service.dtos.HabitWithUserDTO;
import com.example.habit_service.dtos.UserDTO;
import com.example.habit_service.enums.Frequency;
import com.example.habit_service.models.Habit;
import com.example.habit_service.repository.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MongoDBContainer;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import reactor.test.StepVerifier;



import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Testcontainers
public class HabitServiceIntegrationTest  {

    @Container
    static MongoDBContainer mongoContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
    }

    @Autowired
    private HabitService habitService;

    @Autowired
    private HabitRepository habitRepository;

    @MockBean // Solo mockeamos lo externo
    private UserClientService userClientService;

    private Habit habit1;
    private UserDTO userDTO;
    private HabitWithUserDTO habitWithUserDTO;
    private List<Habit> habitList;

    @BeforeEach
    void setUp() {
        habit1 = new Habit();
        habit1.setId("h1");
        habit1.setName("Hacer ejercicio");
        habit1.setUserId("u1");
        habit1.setCategory("Health");
        habit1.setDescription("---");
        habit1.setFrequency(Frequency.SOMETIMES);

        habitRepository.save(habit1).block();

        userDTO = new UserDTO();
        userDTO.setId("u1");
        userDTO.setUserName("name1");
        habitWithUserDTO= new HabitWithUserDTO();
        habitWithUserDTO.setHabit(habit1);
        habitWithUserDTO.setUserDTO(userDTO);

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
        StepVerifier.create(habitService.getHabitById(habit1.getId()))
                .assertNext(habit -> assertEquals(habit1.getId(), habit.getId()))
                .verifyComplete();
    }


}
