package com.example.tracker_service.dtos;


import com.example.tracker_service.models.Tracker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackerWithUserAndHabit {

    private Tracker tracker;

    private UserDTO userDTO;

    private HabitDTO habitDTO;
}
