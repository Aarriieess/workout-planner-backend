package com.workoutplanner.workout_planner_api.dto;

public record PlanExerciseResponse(
        Long exerciseId,
        String exerciseName,
        int sets,
        int reps,
        int restSeconds
) {}
