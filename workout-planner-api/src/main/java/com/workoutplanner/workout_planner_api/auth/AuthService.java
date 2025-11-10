package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.auth.RefreshTokenHasher;
import com.workoutplanner.workout_planner_api.config.ResourceNotFoundException;
import com.workoutplanner.workout_planner_api.dto.AuthResponse;
import com.workoutplanner.workout_planner_api.dto.LoginRequest;
import com.workoutplanner.workout_planner_api.dto.RefreshTokenRequest;
import com.workoutplanner.workout_planner_api.dto.SignupRequest;
import com.workoutplanner.workout_planner_api.model.RefreshToken;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.repo.RefreshTokenRepo;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepo refreshTokenRepo;
    private final RefreshTokenHasher hasher;


    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getId());
        RefreshToken refreshToken = createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getRawToken(),
                refreshToken.getExpiryDate(),
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public AuthResponse signup(SignupRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        User newUser = userRepo.save(user);

        String accessToken = jwtService.generateAccessToken(newUser.getEmail(), newUser.getId());
        RefreshToken refreshToken = createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getRawToken(),
                refreshToken.getExpiryDate(),
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    private String generateRandomToken() {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private RefreshToken createRefreshToken(User user) {
        String rawRefreshToken = generateRandomToken();
        String hashedRefreshToken = hasher.hash(rawRefreshToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshTokenHashed(hashedRefreshToken)
                .rawToken(rawRefreshToken)
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        return refreshTokenRepo.save(refreshToken);
    }

    public AuthResponse refreshAccessToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        RefreshToken userRefreshToken = refreshTokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));

        if (!userRefreshToken.isActive()) {
            throw new AccessDeniedException("Refresh token has expired. Please login again.");
        }

        User user = userRefreshToken.getUser();
        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), user.getId());

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                userRefreshToken.getExpiryDate(),
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    @Transactional
    public void logout(
            Long userId,
            @Nullable String refreshToken,
            boolean allDevices)
    {
        if (allDevices) {
            refreshTokenRepo.revokeAllByUserId(userId);
        }
        else {
            if (refreshToken == null) {
                throw new IllegalArgumentException("Refresh token is required for single-device logout");
            }

            int updated = refreshTokenRepo.revokeByTokenAndUserId(refreshToken, userId);
            if (updated == 0) {
                throw new ResourceNotFoundException("Invalid refresh token");
            }
        }
    }
}
