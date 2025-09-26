package com.workoutplanner.workout_planner_api.dto;

public record AuthResponse(
        String token,
        Long userId,
        String email,
        String name
) {
}
