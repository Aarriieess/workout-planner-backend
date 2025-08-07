package com.workoutplanner.workout_planner_api.service.strategy;

import com.workoutplanner.workout_planner_api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BroSplitStrategy extends BaseWorkoutStrategy {
    @Override
    public List<PlanExercise> generatePlan(UserProfile userProfile, Map<MuscleGroup, List<Exercise>> movementMap, WorkoutTemplate template) {

        List<PlanExercise> broSplitRoutine = new ArrayList<>();
        int trainingDays = userProfile.getTrainingDays();

        for (int day = 1; day <= trainingDays; day++) {
            int finalDay = day;
            generateBroSplit(movementMap, day).forEach(exercise -> {
                PlanExercise planExercise = createPlanExercise(exercise, template, userProfile);
                planExercise.setDayIndex(finalDay);
                broSplitRoutine.add(planExercise);
            });
        }

        return broSplitRoutine;
    }

    @Override
    public WorkoutSplit getSupportedSplit() {
        return WorkoutSplit.BRO_SPLIT;
    }

    public List<Exercise> generateBroSplit (Map<MuscleGroup, List<Exercise>> movementMap, int days) {

        List<Exercise> broSplit = new ArrayList<>();

        int routineDays = ((days - 1) % 6) + 1;

        switch (routineDays){
            case 1:
                broSplit.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.CHEST, List.of()), 5));
                break;
            case 2:
                broSplit.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.SHOULDERS, List.of()), 5));
                break;
            case 3:
                broSplit.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.BACK, List.of()), 5));
                break;
            case 4:
                broSplit.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.BICEPS, List.of()), 5));
                break;
            case 5:
                broSplit.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.TRICEPS, List.of()), 5));
                break;
            case 6:
                broSplit.addAll(pickRandom(movementMap.getOrDefault(MuscleGroup.LEGS, List.of()), 5));
                break;
            default:
                return new  ArrayList<>();
        };

        return broSplit;
    }
}
