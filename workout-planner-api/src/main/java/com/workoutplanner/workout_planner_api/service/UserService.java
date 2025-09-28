package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.dto.SignupRequest;
import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    private UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User createdUserFromSignup(SignupRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(encodedPassword);

        return userRepo.save(user);
    }

    public User getUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserProfile updateUserProfile(Long userId, UserProfileRequest request) {
        User user = getUser(userId);
        UserProfile profile = user.getUserProfile();

        profile.setFitnessGoal(request.getFitnessGoal());
        profile.setFitnessLevel(request.getFitnessLevel());
        profile.setTrainingDays(request.getTrainingDays());
        profile.setTrainingDays(request.getTrainingDays());

        return userRepo.save(user).getUserProfile();
    }
}
