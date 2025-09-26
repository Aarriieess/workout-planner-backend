package com.workoutplanner.workout_planner_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record SignupRequest(
        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required")
        String email,

        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) { }
