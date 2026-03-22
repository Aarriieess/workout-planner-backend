package com.workoutplanner.workout_planner_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaneExerciseUpdateRequest {
    @NotNull(message = "PlanExercise ID is required")
    private Long planExerciseId;

    @Min(value = 1, message = "must be at least 1")
    private Integer sets;

    @Min(value = 1, message = "must be at least 1")
    private Integer reps;

    @Min(value = 1, message = "must be at least 1")
    private Integer restSeconds;

    @Min(value = 1, message = "must be at least 1")
    @Max(value = 7, message = "must be at most 7")
    private Integer dayIndex;
}
