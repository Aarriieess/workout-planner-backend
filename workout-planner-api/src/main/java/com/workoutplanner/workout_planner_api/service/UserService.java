package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.config.ResourceNotFoundException;
import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.dto.UserProfileResponse;
import com.workoutplanner.workout_planner_api.dto.UserResponse;
import com.workoutplanner.workout_planner_api.mapper.UserMapper;
import com.workoutplanner.workout_planner_api.mapper.UserProfileMapper;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final UserProfileMapper userProfileMapper;
    private final UserMapper userMapper;

    public UserProfile getUserProfileEntity(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new ResourceNotFoundException("User profile not found");
        }

        return profile;
    }

    public UserResponse getUserResponse(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toResponse(user);
    }

    public User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserProfile profile = user.getUserProfile();

        if (profile == null) {
            throw new ResourceNotFoundException("User profile not found");
        }

        return userProfileMapper.toResponse(profile);
    }

    @Transactional
    public UserProfileResponse upsertUserProfile(Long userId, UserProfileRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserProfile profile = user.getUserProfile();

        if (profile == null) {
            profile = userProfileMapper.toEntity(request);
            profile.setUser(user);
            user.setUserProfile(profile);
        } else {
            userProfileMapper.updateEntityFromRequest(request, profile);
        }

        userRepo.save(user);
        return userProfileMapper.toResponse(profile);
    }

    public void validateRequest(UserProfileRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (request.getTrainingDays() < 1 || request.getTrainingDays() > 7) {
            throw new IllegalArgumentException("Training days must be between 1 and 6");
        }
    }


}
