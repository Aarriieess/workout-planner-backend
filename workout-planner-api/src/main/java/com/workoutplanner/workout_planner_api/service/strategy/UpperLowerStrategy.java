package com.workoutplanner.workout_planner_api.service.strategy;

import com.workoutplanner.workout_planner_api.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class UpperLowerStrategy extends BaseWorkoutStrategy {

    @Override
    public List<PlanExercise> generatePlan(Map<MovementPattern, List<Exercise>> movementMap,
                                           WorkoutTemplate workoutTemplate, UserProfile userProfile) {

        List<PlanExercise> planExercises = new ArrayList<>();

        List<Exercise> pushExercise = pickRandom(
                movementMap.getOrDefault(MovementPattern.PUSH, List.of()), 3
        );

        List<Exercise> pullExercise = pickRandom(
                movementMap.getOrDefault(MovementPattern.PULL, List.of()), 3
        );

        List<Exercise> allLowerExercises = Stream.of(
                movementMap.getOrDefault(MovementPattern.SQUAT, List.of()),
                movementMap.getOrDefault(MovementPattern.HINGE, List.of()),
                movementMap.getOrDefault(MovementPattern.LUNGE, List.of())
        ).flatMap(Collection::stream).toList();

        List<Exercise> lowerExercises = pickRandom(allLowerExercises, 6);

        List<Exercise> coreExercises = pickRandom(
                movementMap.getOrDefault(MovementPattern.CORE, List.of()), 1
        );

        List<Exercise> upperRoutine = new ArrayList<>();
        upperRoutine.addAll(pushExercise);
        upperRoutine.addAll(pullExercise);

        List<Exercise> lowerRoutine = new ArrayList<>();
        lowerRoutine.addAll(lowerExercises);
        lowerRoutine.addAll(coreExercises);


        //NOT WORKING YET
        return planExercises;
    }

    @Override
    public WorkoutSplit getSupportedSplit() {
        return WorkoutSplit.UPPER_LOWER;
    }
}
