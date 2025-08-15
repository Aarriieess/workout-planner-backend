package com.workoutplanner.workout_planner_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlanExerciseRequest {

    @NotNull
    @Min(1)
    private Integer sets;

    @NotNull
    @Min(1)
    private Integer reps;

    @NotNull
    @Min(1)
    private Integer restSeconds;
}
