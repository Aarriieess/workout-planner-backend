package com.workoutplanner.workout_planner_api.service.strategy;

import com.workoutplanner.workout_planner_api.model.*;

import java.util.List;
import java.util.Map;

public interface WorkoutGenerationStrategy {

    List<PlanExercise> generatePlan(Map<MovementPattern, List<Exercise>> movementMap,
                                    WorkoutTemplate workoutTemplate, UserProfile userProfile);

    WorkoutSplit getSupportedSplit();
}
