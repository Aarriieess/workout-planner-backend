package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.auth.JwtService;
import com.workoutplanner.workout_planner_api.dto.AuthResponse;
import com.workoutplanner.workout_planner_api.dto.LoginRequest;
import com.workoutplanner.workout_planner_api.dto.SignupRequest;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserRepo userRepo,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
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

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getId()
        );

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public AuthResponse signup(SignupRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User newUser = userRepo.save(user);

        String token = jwtService.generateToken(newUser.getEmail(), newUser.getId());

        return new AuthResponse(
                token,
                newUser.getId(),
                newUser.getEmail(),
                newUser.getName()
        );
    }

}
