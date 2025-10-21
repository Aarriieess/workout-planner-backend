package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.FitnessLevel;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import com.workoutplanner.workout_planner_api.model.WorkoutEnvironment;

public record UserProfileResponse(
        FitnessLevel fitnessLevel,
        FitnessGoal fitnessGoal,
        WorkoutEnvironment workoutEnvironment,
        int trainingDays
) {}
