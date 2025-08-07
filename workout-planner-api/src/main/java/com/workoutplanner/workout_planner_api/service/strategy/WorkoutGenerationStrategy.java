package com.workoutplanner.workout_planner_api.service.strategy;

import com.workoutplanner.workout_planner_api.model.*;

import java.util.List;
import java.util.Map;

public interface WorkoutGenerationStrategy {

    List<PlanExercise> generatePlan(UserProfile userProfile,
                                    Map<MuscleGroup, List<Exercise>> movementMap,
                                    WorkoutTemplate template);

    WorkoutSplit getSupportedSplit();
}
