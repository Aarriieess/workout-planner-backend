package com.workoutplanner.workout_planner_api.service.strategy;

import com.workoutplanner.workout_planner_api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PPLStrategy extends BaseWorkoutStrategy {

    @Override
    public List<PlanExercise> generatePlan(UserProfile userProfile, Map<MuscleGroup, List<Exercise>> movementMap, WorkoutTemplate template) {

        List<PlanExercise> pplRoutine = new ArrayList<>();
        int trainingDays = userProfile.getTrainingDays();

        for (int day = 1; day <= trainingDays; day++) {
            int finalDay = day;
            generatePPLRoutine(movementMap, day).forEach(exercise -> {
                PlanExercise planExercise = createPlanExercise(exercise, template, userProfile);
                planExercise.setDayIndex(finalDay);
                pplRoutine.add(planExercise);
            });
        }

        return pplRoutine;
    }

    @Override
    public WorkoutSplit getSupportedSplit() {
        return WorkoutSplit.PPL;
    }

    public List<Exercise> generatePPLRoutine(Map<MuscleGroup, List<Exercise>> movementMap, int days) {

        int routineDays = ((days - 1) % 3) + 1;

        return switch (routineDays) {
            case 1 -> generatePushRoutine(movementMap, days);
            case 2 -> generatePullRoutine(movementMap, days);
            case 3 -> generateLegsRoutine(movementMap, days);
            default -> new ArrayList<>();
        };
    }


    public List<Exercise> generatePushRoutine(Map<MuscleGroup, List<Exercise>> movementMap, int days) {

        List<Exercise> pushExercises = new ArrayList<>();

        pushExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.CHEST, List.of()), 2));
        pushExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.SHOULDERS, List.of()), 2));
        pushExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.TRICEPS, List.of()), 2));

        return pushExercises;
    }

    public List<Exercise> generatePullRoutine(Map<MuscleGroup, List<Exercise>> movementMap, int days) {

        List<Exercise> pullExercises = new ArrayList<>();

        pullExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.BACK, List.of()), 3));
        pullExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.BICEPS, List.of()), 2));

        return pullExercises;
    }

    public List<Exercise> generateLegsRoutine(Map<MuscleGroup, List<Exercise>> movementMap, int days) {

        return new ArrayList<>(pickRandom(movementMap.getOrDefault(MuscleGroup.LEGS, List.of()), 6));
    }

}
