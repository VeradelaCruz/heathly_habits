package com.example.tracker_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@Document
public class Tracker {

    @Id
    String id;

    @NotBlank(message = "Please assign a user.")
    String userId;

    @NotBlank(message = "Please assign a habit.")
    String habitId;

    @NotNull
    @CreatedDate
    LocalDate date;

    @NotNull(message = "Completion status is required.")
    Boolean completed;

    @Size(max = 300)
    String notes;
}
