package com.workoutplanner.workout_planner_api.dto;

import java.time.Instant;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,  // always "Bearer"
        Instant accessTokenExpiresAt,
        Long userId,
        String email,
        String name
) {
    public AuthResponse(String accessToken, String refreshToken, Instant accessTokenExpiresAt,
                        Long userId, String email, String name) {
        this(accessToken, refreshToken, "Bearer", accessTokenExpiresAt, userId, email, name);
    }
}
