package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.FitnessLevel;
import com.workoutplanner.workout_planner_api.model.WorkoutEnvironment;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UserProfileRequest {
    private Long userId;

    @NotNull
    private FitnessLevel fitnessLevel;

    @NotNull
    private FitnessGoal fitnessGoal;

    @NotNull
    private WorkoutEnvironment workoutEnvironment;

    @Min(1)
    @Max(7)
    private int trainingDays;
}
