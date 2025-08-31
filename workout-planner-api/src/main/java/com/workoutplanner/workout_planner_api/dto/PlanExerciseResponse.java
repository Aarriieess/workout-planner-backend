package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.PlanExercise;

public record PlanExerciseResponse(
        Long exerciseId,
        String exerciseName,
        int sets,
        int reps,
        int restSeconds
) {
    public static PlanExerciseResponse fromEntity(PlanExercise planExercise) {
        return new PlanExerciseResponse(
                planExercise.getId(),
                planExercise.getExercise().getName(),
                planExercise.getSets(),
                planExercise.getReps(),
                planExercise.getRestSeconds()
        );
    }
}
