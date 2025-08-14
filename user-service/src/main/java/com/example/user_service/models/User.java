package com.example.user_service.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Document
public class User {

    @Id
    private String id;

    @NotBlank(message = "A name must be provided")
    @Size(max = 20, message = "Username must be at most 20 characters")
    @Indexed(unique = true)
    private String userName;

    @NotBlank(message = "Email must be provided")
    @Email(message = "Email must be valid")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "Password must be provided")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @CreatedDate
    private LocalDate createdAt;

    private String roles;
}
