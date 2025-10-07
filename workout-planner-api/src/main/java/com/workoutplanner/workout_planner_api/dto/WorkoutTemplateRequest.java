package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.WorkoutSplit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutTemplateRequest {

    @NotBlank(message = "Template name is required")
    String name;

    @NotNull(message = "Fitness goal is required")
    FitnessGoal fitnessGoal;

    @NotNull(message = "Workout split is required")
    WorkoutSplit workoutSplit;

    @Valid
    @NotEmpty(message = "Exercises cannot be empty")
    List<PlanExerciseRequest> planExerciseRequestList;
}
