package com.workoutplanner.workout_planner_api.service.strategy;

import com.workoutplanner.workout_planner_api.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.*;


public abstract class BaseWorkoutStrategy implements WorkoutGenerationStrategy{

    protected List<Exercise> pickRandom(List<Exercise> list, int count){
        if (list == null || list.isEmpty() || count == 0) return List.of();
        List<Exercise> exerciseList = new ArrayList<>(list);
        Collections.shuffle(exerciseList);
        return exerciseList.stream().limit(count).toList();
    }

    protected PlanExercise createPlanExercise(Exercise exercise, WorkoutTemplate workoutTemplate, UserProfile userProfile){
        if (exercise == null) {
            System.err.println("This is an error message.");
            return null;
        }

        PlanExercise planExercise = new PlanExercise();
        planExercise.setExercise(exercise);
        planExercise.setWorkoutTemplate(workoutTemplate);

        setExerciseParameters(planExercise, userProfile);

        return planExercise;
    }

    private void setExerciseParameters(PlanExercise planExercise, UserProfile userProfile) {
        switch (userProfile.getFitnessGoal()) {
            case STRENGTH:
                planExercise.setSets(5);
                planExercise.setReps(5);
                planExercise.setRestSeconds(180);
                break;
            case HYPERTROPHY:
                planExercise.setSets(3);
                planExercise.setReps(10);
                planExercise.setRestSeconds(90);
                break;
            case ENDURANCE:
                planExercise.setSets(2);
                planExercise.setReps(15);
                planExercise.setRestSeconds(60);
                break;
            default:
                planExercise.setSets(3);
                planExercise.setReps(10);
                planExercise.setRestSeconds(90);
                break;
        }

        adjustForFitnessLevel(planExercise, userProfile.getFitnessLevel());
    }

    private void adjustForFitnessLevel(PlanExercise planExercise, FitnessLevel fitnessLevel) {
        switch (fitnessLevel) {
            case BEGINNER:
                planExercise.setSets(Math.max(2, planExercise.getSets() - 1));
                planExercise.setRestSeconds(planExercise.getRestSeconds() + 30);
                break;
            case ADVANCED:
                planExercise.setSets(planExercise.getSets() + 1);
                break;
            case INTERMEDIATE:
                break;
        }
    }

}
