package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
@Data
public class ExerciseRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Primary muscle is required")
    private MuscleGroup primaryMuscleGroup;

    private List<MuscleGroup> secondaryMuscleGroup;

    private String description;

    private List<FitnessGoal> targetGoals;

    private List<FitnessLevel> suitableLevels;

    private List<WorkoutEnvironment> workoutEnvironment;

    @NotNull(message = "Exercise type is required")
    private ExerciseType exerciseType;

    private boolean isUnilateral;
}
