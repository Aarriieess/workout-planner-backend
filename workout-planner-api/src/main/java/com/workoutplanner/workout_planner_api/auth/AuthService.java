package com.workoutplanner.workout_planner_api.auth;

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
import java.util.List;

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
                accessTokenExp(accessToken),
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
        RefreshToken refreshToken = createRefreshToken(newUser);


        return new AuthResponse(
                accessToken,
                refreshToken.getRawToken(),
                accessTokenExp(accessToken),
                newUser.getId(),
                newUser.getEmail(),
                newUser.getName()
        );

    }

    @Transactional
    public void logout(Long userId, @Nullable String refreshToken, boolean allDevices) {
        if (allDevices) {
            refreshTokenRepo.revokeAllByUserId(userId);
            deleteRevokedTokens(userId);
        } else {
            logoutSingleDevice(userId, refreshToken);
        }
    }

    private void logoutSingleDevice(Long userId, String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token is required for single-device logout");
        }

        RefreshToken token = findAndEvaluateToken(refreshToken);

        if (!token.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Token doesn't belong to this user");
        }

        token.setRevoked(true);
        refreshTokenRepo.save(token);
        deleteRevokedTokens(userId);
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
                .hashedToken(hashedRefreshToken)
                .rawToken(rawRefreshToken)
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        return refreshTokenRepo.save(refreshToken);
    }

    @Transactional
    public AuthResponse refreshAccessTokens(RefreshTokenRequest request) {
        String rawToken = request.refreshToken();
        RefreshToken oldToken = findAndEvaluateToken(rawToken);

        User user = oldToken.getUser();
        RefreshToken newToken = createRefreshToken(user);

        oldToken.setRevoked(true);
        oldToken.setReplacedByToken(newToken.getHashedToken());
        refreshTokenRepo.save(oldToken);
        deleteRevokedTokens(user.getId());

        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), user.getId());

        return new AuthResponse(
                newAccessToken,
                newToken.getRawToken(),
                accessTokenExp(newAccessToken),
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    private RefreshToken findAndEvaluateToken(String rawToken) {
        String userEmail = jwtService.extractEmail(rawToken);

        List<RefreshToken> tokens = refreshTokenRepo.findAllByUserEmail(userEmail);

        RefreshToken token = tokens.stream()
                .filter(t -> hasher.matches(rawToken, t.getHashedToken()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));

        if (!token.isActive()) {
            throw new AccessDeniedException("Refresh token has expired. Please login again.");
        }

        return token;
    }

    private void deleteRevokedTokens(Long userId) {
        refreshTokenRepo.deleteByUserIdAndRevokedTrue(userId);
    }

    private Instant accessTokenExp(String accessToken) {
        return jwtService.extractExpiration(accessToken).toInstant();
    }
}
