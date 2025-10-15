package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.*;
import lombok.Builder;

import java.util.List;

@Builder
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
        return ExerciseResponse.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .primaryMuscleGroup(exercise.getPrimaryMuscleGroup())
                .secondaryMuscleGroup(exercise.getSecondaryMuscleGroup())
                .description(exercise.getDescription())
                .targetGoals(exercise.getTargetGoals())
                .suitableLevels(exercise.getSuitableLevels())
                .workoutEnvironment(exercise.getWorkoutEnvironment())
                .exerciseType(exercise.getExerciseType())
                .isUnilateral(exercise.isUnilateral())
                .build();

    }
}
