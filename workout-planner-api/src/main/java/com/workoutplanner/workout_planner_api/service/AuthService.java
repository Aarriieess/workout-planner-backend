package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.auth.JwtService;
import com.workoutplanner.workout_planner_api.config.ResourceNotFoundException;
import com.workoutplanner.workout_planner_api.dto.AuthResponse;
import com.workoutplanner.workout_planner_api.dto.LoginRequest;
import com.workoutplanner.workout_planner_api.dto.RefreshTokenRequest;
import com.workoutplanner.workout_planner_api.dto.SignupRequest;
import com.workoutplanner.workout_planner_api.model.RefreshToken;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.repo.RefreshTokenRepo;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Ref;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepo refreshTokenRepo;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserRepo userRepo,
                       PasswordEncoder passwordEncoder,
                       RefreshTokenRepo refreshTokenRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user.getEmail(), user.getId());
        RefreshToken refreshToken = createRefreshToken(user);

        return new AuthResponse(
                token,
                refreshToken.getToken(),
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

        String token = jwtService.generateToken(newUser.getEmail(), newUser.getId());
        RefreshToken refreshToken = createRefreshToken(user);

        return new AuthResponse(
                token,
                refreshToken.getToken(),
                newUser.getId(),
                newUser.getEmail(),
                newUser.getName()
        );
    }

    private String generateRandomToken() {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(generateRandomToken())
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();
        return refreshTokenRepo.save(refreshToken);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        RefreshToken token = refreshTokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(Instant.now())){
            throw new AccessDeniedException("Refresh token has expired. Please login again.");
        }

        User user = token.getUser();
        String newAccessToken = jwtService.generateToken(user.getEmail(), user.getId());

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public void logout(Long userId){
        refreshTokenRepo.deleteByUserId(userId);
    }

}
