package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.PlanExercise;

public record PlanExerciseResponse(
        Long exerciseId,
        String exerciseName,
        int sets,
        int reps,
        int restSeconds
) {}
