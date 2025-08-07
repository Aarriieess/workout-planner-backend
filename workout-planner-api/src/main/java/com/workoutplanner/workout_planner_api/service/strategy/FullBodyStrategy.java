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
    public List<PlanExercise> generatePlan(UserProfile userProfile, Map<MuscleGroup, List<Exercise>> movementMap,
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

    public List<Exercise> generateFullBodyRoutine(Map<MuscleGroup, List<Exercise>> movementMap) {

        System.out.println("=== Movement Map Contents ===");
        for (MuscleGroup group : MuscleGroup.values()) {
            List<Exercise> exercises = movementMap.getOrDefault(group, List.of());
            System.out.println(group + ": " + exercises.size() + " exercises");
            if (!exercises.isEmpty()) {
                exercises.forEach(ex -> System.out.println("  - " + ex.getName()));
            }
        }

        return Stream.of(
                pickRandom(movementMap.getOrDefault(MuscleGroup.CHEST, List.of()), 1),
                pickRandom(movementMap.getOrDefault(MuscleGroup.SHOULDERS, List.of()), 1),
                pickRandom(movementMap.getOrDefault(MuscleGroup.TRICEPS, List.of()), 1),
                pickRandom(movementMap.getOrDefault(MuscleGroup.BACK, List.of()), 1),
                pickRandom(movementMap.getOrDefault(MuscleGroup.LEGS, List.of()), 2),
                pickRandom(movementMap.getOrDefault(MuscleGroup.CORE, List.of()), 1)
        ).flatMap(Collection::stream).toList();

    }
}