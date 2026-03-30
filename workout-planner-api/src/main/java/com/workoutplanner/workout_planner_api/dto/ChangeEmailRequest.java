package com.workoutplanner.workout_planner_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeEmailRequest {
    @NotBlank(message = "Old password is required")
    private String currentPassword;

    @Email
    @NotBlank(message = "New email is required")
    private String newEmail;
}
