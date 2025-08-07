package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.FitnessLevel;
import com.workoutplanner.workout_planner_api.model.WorkoutEnvironment;
import com.workoutplanner.workout_planner_api.model.WorkoutSplit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileRequest {
    private Long userId;

    @NotNull
    private FitnessLevel fitnessLevel;

    @NotNull
    private FitnessGoal fitnessGoal;

    @NotNull
    private WorkoutEnvironment workoutEnvironment;

    @NotNull
    private WorkoutSplit workoutSplit;

    @Min(1)
    private int trainingDays;
}
