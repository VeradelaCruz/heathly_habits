package com.example.habit_service.dtos;

import com.example.habit_service.models.Habit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitWithUserDTO{
    Habit habit;
    UserDTO userDTO;
}
