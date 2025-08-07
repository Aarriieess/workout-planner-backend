package com.workoutplanner.workout_planner_api.service.strategy;

import com.workoutplanner.workout_planner_api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class UpperLowerStrategy extends BaseWorkoutStrategy{

    @Override
    public List<PlanExercise> generatePlan(UserProfile userProfile, Map<MuscleGroup,
            List<Exercise>> movementMap, WorkoutTemplate template) {

        List<PlanExercise> upperLowerRoutine = new ArrayList<>();
        int trainingDays = userProfile.getTrainingDays();

        for(int day = 1; day <= trainingDays; day++) {
            int finalDay = day;
            generateUpperLowerRoutine(movementMap, day).forEach(exercise -> {
                PlanExercise planExercise = createPlanExercise(exercise, template, userProfile);
                planExercise.setDayIndex(finalDay);
                upperLowerRoutine.add(planExercise);
            });
        }

        return upperLowerRoutine;
    }

    @Override
    public WorkoutSplit getSupportedSplit() {
        return WorkoutSplit.UPPER_LOWER;
    }

    public List<Exercise> generateUpperLowerRoutine(Map<MuscleGroup, List<Exercise>> movementMap, int day) {

        boolean isUpperDay = (day - 1) % 2 == 0;

        if (isUpperDay) {
            return generateUpperRoutine(movementMap, day);
        } else {
            return generateLowerRoutine(movementMap, day);
        }
    }

    public List<Exercise> generateUpperRoutine(Map<MuscleGroup, List<Exercise>> movementMap, int day) {

        List<Exercise> upperExercises = new ArrayList<>();

        int upperDayNumber = ((day - 1) / 2 ) + 1;
        boolean isUpperA = day % 2 == 1;

        if (isUpperA) {
            upperExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.BACK, List.of()), 3));
            upperExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.SHOULDERS, List.of()), 2));
            upperExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.BICEPS, List.of()), 2));
        } else {
            upperExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.CHEST, List.of()), 3));
            upperExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.SHOULDERS, List.of()), 2));
            upperExercises.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.TRICEPS, List.of()), 2));
        }

        return upperExercises;
    }

    public List<Exercise> generateLowerRoutine(Map<MuscleGroup, List<Exercise>> movementMap, int day) {


        int lowerDayNumber = (day / 2);
        boolean isLowerA = lowerDayNumber % 2 == 1;

        int exerciseCount = isLowerA ? 6 : 5;

        return new ArrayList<>(pickRandom(movementMap.getOrDefault(MuscleGroup.LEGS, List.of()), exerciseCount));
    }

}
