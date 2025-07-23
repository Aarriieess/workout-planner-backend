package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.MovementPattern;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;

import java.util.List;
import java.util.Map;

public interface RuleBasedWorkoutService {
    WorkoutTemplate generateTemplate(UserProfile userProfile);
}
