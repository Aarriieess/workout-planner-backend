package com.workoutplanner.workout_planner_api.dto;

public record AuthResponse(
        String token,
        String refreshToken,
        Long userId,
        String email,
        String name
) {
}
