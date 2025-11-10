package com.workoutplanner.workout_planner_api.auth;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;

public class RefreshTokenHasher {
    private final String secret;

    public RefreshTokenHasher(@Value("${app.security.refresh-token-secret}") String secret) {
        this.secret = secret;
    }

    public String hash(String token) {
        return DigestUtils.sha256Hex(token + secret);
    }
}
