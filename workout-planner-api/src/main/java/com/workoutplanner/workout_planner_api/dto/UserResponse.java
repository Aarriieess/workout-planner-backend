package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.model.UserProfile;

import java.util.Locale;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserProfileResponse profile
) { }
