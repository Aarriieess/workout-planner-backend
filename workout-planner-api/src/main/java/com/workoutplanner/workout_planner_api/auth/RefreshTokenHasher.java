package com.workoutplanner.workout_planner_api.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class RefreshTokenHasher {


    public String hash(String refreshToken) {
       try {
           MessageDigest digest = MessageDigest.getInstance("SHA-256");
           byte[] hashBytes = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
           return Base64.getEncoder().encodeToString(hashBytes);
       } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException("SHA-256 algorithm not available", e);
       }
    }

    public boolean matches(String rawToken, String hashedToken) {
        String rawTokenHash = hash(rawToken);
        return rawTokenHash.equals(hashedToken);
    }
}
