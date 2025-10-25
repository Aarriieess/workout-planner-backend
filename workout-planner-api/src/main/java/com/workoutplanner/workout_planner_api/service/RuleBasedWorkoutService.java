package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;


public interface RuleBasedWorkoutService {
    WorkoutTemplateResponse generateTemplate(UserProfileRequest request);
}
