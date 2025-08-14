package com.example.habit_service.controller;

import com.example.habit_service.enums.Frequency;
import com.example.habit_service.models.Habit;
import com.example.habit_service.service.HabitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class HabitControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private HabitService habitService;

    @Test
    public void getAllHabitsTest_ShouldReturnList() {
        List<Habit> mockHabits = List.of(
                Habit.builder()
                        .id("1")
                        .name("Beber agua")
                        .description("Beber al menos 2 litros por día")
                        .createdAt(LocalDate.now())
                        .userId("user1")
                        .category("Salud")
                        .frequency(Frequency.USUALLY)
                        .build(),
                Habit.builder()
                        .id("2")
                        .name("Hacer ejercicio")
                        .description("Ir al gym 2 horas al dia")
                        .createdAt(LocalDate.now())
                        .userId("user2")
                        .category("Entrenamiento")
                        .frequency(Frequency.USUALLY)
                        .build()
        );

        when(habitService.getAllHabits()).thenReturn(Flux.fromIterable(mockHabits));

        webTestClient.get()
                .uri("/habits/getAll")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Habit.class)
                .value(list -> assertTrue(list.size() > 0))
                .consumeWith(response -> {
                    List<Habit> habits = response.getResponseBody();
                    assertNotNull(habits);
                });
    }


    @Test
    public void getHabitById_ShouldReturnMono() {
        Habit habit= Habit.builder()
                .id("1")
                .name("Beber agua")
                .description("Beber al menos 2 litros por día")
                .createdAt(LocalDate.now())
                .userId("user1")
                .category("Salud")
                .frequency(Frequency.USUALLY)
                .build();

        when(habitService.getHabitById(habit.getId())).thenReturn(Mono.just(habit));

        webTestClient.get()
                .uri("/habits/{id}", habit.getId())
                .exchange() //--> ejecuta el llamado de get
                .expectStatus().isOk()
                .expectBody(Habit.class)
                .consumeWith(response -> {
                    Habit habit1 = response.getResponseBody();
                    assertNotNull(habit1);
                    assertEquals(habit.getId(), habit1.getId());
                });
    }


}