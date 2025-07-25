package com.workoutplanner.workout_planner_api.service.strategy;

import com.workoutplanner.workout_planner_api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class FullBodyStrategy extends BaseWorkoutStrategy {

    @Override
    public List<PlanExercise> generatePlan(UserProfile userProfile, Map<MovementPattern, List<Exercise>> movementMap,
                                           WorkoutTemplate workoutTemplate) {

        List<PlanExercise> fullBodyRoutine = new ArrayList<>();
        int trainingDays = userProfile.getTrainingDays();

        for (int day = 1; day <= trainingDays; day++) {
            int finalDay = day;
            generateFullBodyRoutine(movementMap).forEach(exercise -> {
                PlanExercise planExercise = createPlanExercise(exercise, workoutTemplate, userProfile);
                planExercise.setDayIndex(finalDay);
                fullBodyRoutine.add(planExercise);
            });
        }

        return fullBodyRoutine;
    }

    @Override
    public WorkoutSplit getSupportedSplit() {
        return WorkoutSplit.FULL_BODY;
    }

    public List<Exercise> generateFullBodyRoutine(Map<MovementPattern, List<Exercise>> movementMap) {

        List<Exercise> lowerBodyExercises =
                Stream.of(
                        movementMap.getOrDefault(MovementPattern.SQUAT, List.of()),
                        movementMap.getOrDefault(MovementPattern.HINGE, List.of()),
                        movementMap.getOrDefault(MovementPattern.LUNGE, List.of())
                ).flatMap(Collection::stream).toList();


        return Stream.of(
                pickRandom(movementMap.getOrDefault(MovementPattern.PUSH, List.of()), 2),
                pickRandom(movementMap.getOrDefault(MovementPattern.PULL, List.of()), 2),
                pickRandom(lowerBodyExercises, 2),
                pickRandom(movementMap.getOrDefault(MovementPattern.CORE, List.of()), 1)
        ).flatMap(Collection::stream).toList();

    }
}
