package com.workoutplanner.workout_planner_api.service.strategy;

import com.workoutplanner.workout_planner_api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class FullyBodyStrategy extends BaseWorkoutStrategy {

    @Override
    public List<PlanExercise> generatePlan(Map<MovementPattern, List<Exercise>> movementMap,
                                           WorkoutTemplate workoutTemplate, UserProfile userProfile) {

        List<PlanExercise> planExercises = new ArrayList<>();

        List<Exercise> pushExercises = pickRandom(
                movementMap.getOrDefault(MovementPattern.PUSH, List.of()),2
        );

        List<Exercise> pullExercises = pickRandom(
                movementMap.getOrDefault(MovementPattern.PULL, List.of()), 2
        );

        List<Exercise> allLowerExercises = Stream.of(
                movementMap.getOrDefault(MovementPattern.SQUAT, List.of()),
                movementMap.getOrDefault(MovementPattern.HINGE, List.of()),
                movementMap.getOrDefault(MovementPattern.LUNGE, List.of())
        ).flatMap(Collection::stream).toList();

        List<Exercise> lowerExercises = pickRandom(allLowerExercises, 2);

        List<Exercise> coreExercise = pickRandom(
                movementMap.getOrDefault(MovementPattern.CORE, List.of()), 1
        );

        List<Exercise> allSelectedExercises = new ArrayList<>();
        allSelectedExercises.addAll(pushExercises);
        allSelectedExercises.addAll(pullExercises);
        allSelectedExercises.addAll(lowerExercises);
        allSelectedExercises.addAll(coreExercise);

        for (Exercise exercise : allSelectedExercises) {
            planExercises.add(createPlanExercise(exercise, workoutTemplate, userProfile));
        }

        return planExercises;
    }

    @Override
    public WorkoutSplit getSupportedSplit() {
        return WorkoutSplit.FULL_BODY;
    }
}
