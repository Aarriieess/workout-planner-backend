package com.workoutplanner.workout_planner_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest{
        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required")
        String email;

        @NotBlank(message = "Password is required")
        String password;

}
