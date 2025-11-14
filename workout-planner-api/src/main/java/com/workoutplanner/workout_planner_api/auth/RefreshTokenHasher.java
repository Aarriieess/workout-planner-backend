package com.workoutplanner.workout_planner_api.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenHasher {

    private final PasswordEncoder passwordEncoder;

    public String hash(String token) {
        return passwordEncoder.encode(token);
    }

    public boolean matches(String raw, String hashed) {
        return passwordEncoder.matches(raw, hashed);
    }
}
