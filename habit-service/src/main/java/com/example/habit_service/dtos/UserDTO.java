package com.example.habit_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;
    private String userName;
    private String email;
    private String password;
    private LocalDate createdAt;
    private String roles;
}