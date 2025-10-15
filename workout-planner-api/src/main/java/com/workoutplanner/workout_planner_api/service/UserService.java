package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.config.ResourceNotFoundException;
import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.dto.UserProfileResponse;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    private UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UserProfileRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserProfile profile = Optional.ofNullable(user.getUserProfile())
                .orElse(new UserProfile());

        profile.setFitnessGoal(request.getFitnessGoal());
        profile.setFitnessLevel(request.getFitnessLevel());
        profile.setWorkoutEnvironment(request.getWorkoutEnvironment());
        profile.setTrainingDays(request.getTrainingDays());

        user.setUserProfile(profile);
        userRepo.save(user);

        return new UserProfileResponse(
                profile.getFitnessLevel(),
                profile.getFitnessGoal(),
                profile.getWorkoutEnvironment(),
                profile.getTrainingDays()
        );
    }
}
