package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.model.*;

import java.util.List;
import java.util.Map;

public interface RuleBasedWorkoutService {
    WorkoutTemplate generateTemplate(UserProfileRequest request);
}
