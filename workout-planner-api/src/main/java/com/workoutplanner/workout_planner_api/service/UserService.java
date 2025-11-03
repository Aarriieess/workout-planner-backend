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

        return userProfileMapper.toResponse(user.getUserProfile());
    }

    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UserProfileRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserProfile profile = Optional.ofNullable(user.getUserProfile())
                .orElse(new UserProfile());

        userProfileMapper.updateEntityFromRequest(request, profile);
        user.setUserProfile(profile);

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

    public UserProfile createUserProfile (UserProfileRequest request) {
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getUserProfile() != null) {
            throw new IllegalStateException("User already has a profile");
        }
        UserProfile profile = userProfileMapper.toEntity(request);

        user.setUserProfile(profile);
        userRepo.save(user);

        return profile;
    }

}
