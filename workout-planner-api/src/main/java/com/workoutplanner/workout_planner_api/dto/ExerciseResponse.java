package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.*;

import java.util.List;

public record ExerciseResponse(
        Long id,
        String name,
        MuscleGroup primaryMuscleGroup,
        List<MuscleGroup> secondaryMuscleGroup,
        String description,
        List<FitnessGoal> targetGoals,
        List<FitnessLevel> suitableLevels,
        List<WorkoutEnvironment> workoutEnvironment,
        ExerciseType exerciseType,
        boolean isUnilateral
) {
    public static ExerciseResponse fromEntity(Exercise exercise) {
        return new ExerciseResponse(
                exercise.getId(),
                exercise.getName(),
                exercise.getPrimaryMuscleGroup(),
                exercise.getSecondaryMuscleGroup(),
                exercise.getDescription(),
                exercise.getTargetGoals(),
                exercise.getSuitableLevels(),
                exercise.getWorkoutEnvironment(),
                exercise.getExerciseType(),
                exercise.isUnilateral()
        );
    }
}
