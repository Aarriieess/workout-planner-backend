package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

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
