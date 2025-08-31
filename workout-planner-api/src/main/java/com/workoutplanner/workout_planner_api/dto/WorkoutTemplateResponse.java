package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.WorkoutSplit;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;

import java.util.List;

public record WorkoutTemplateResponse(
        Long id,
        String name,
        FitnessGoal fitnessGoal,
        WorkoutSplit workoutSplit,
        List<PlanExerciseResponse> planExercise
) {
    public static WorkoutTemplateResponse fromEntity(WorkoutTemplate template) {
        return new WorkoutTemplateResponse(
                template.getId(),
                template.getName(),
                template.getFitnessGoal(),
                template.getWorkoutSplit(),
                template.getPlanExercises()
                        .stream()
                        .map(PlanExerciseResponse::fromEntity)
                        .toList()
        );
    }
}
