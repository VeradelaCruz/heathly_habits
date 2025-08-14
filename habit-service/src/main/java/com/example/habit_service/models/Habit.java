package com.example.habit_service.models;

import com.example.habit_service.enums.Frequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "habit")
public class Habit {

    @Id
    private String id;

    @NotBlank(message = "A name must be provided.")
    private String name;

    @NotBlank(message = "Please write a description.")
    @Size(max = 200)
    private String description;

    @NotNull
    @CreatedDate
    private LocalDate createdAt;

    @NotNull
    private String userId;

    @NotBlank(message = "This field must be completed.")
    private String category;

    @NotNull(message = "Please specify a frequency.")
    private Frequency frequency;
}
