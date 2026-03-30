package com.workoutplanner.workout_planner_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "new password is required")
    @Size(min = 8, message = "Password at least 8 characters")
    private String newPassword;

    @NotBlank(message = "Re-enter password")
    private String confirmNewPassword;
}
