package com.workoutplanner.workout_planner_api.service.implementation;

import com.workoutplanner.workout_planner_api.model.*;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.service.RuleBasedWorkoutService;
import com.workoutplanner.workout_planner_api.service.strategy.WorkoutGenerationStrategy;
import com.workoutplanner.workout_planner_api.service.strategy.WorkoutStrategyFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RuleBasedWorkoutServiceImpl implements RuleBasedWorkoutService {

    private final ExerciseRepo exerciseRepo;
    private final WorkoutStrategyFactory strategyFactory;

    public RuleBasedWorkoutServiceImpl(ExerciseRepo exerciseRepo, WorkoutStrategyFactory strategyFactory) {
        this.exerciseRepo = exerciseRepo;
        this.strategyFactory = strategyFactory;
    }

    @Override
    public WorkoutTemplate generateTemplate(UserProfile userProfile) {

        List<Exercise> filteredExercises = filterExercises(userProfile);

        WorkoutTemplate template = new WorkoutTemplate();
        template.setName("Smart Plan - " + userProfile.getFitnessGoal().name());
        template.setFitnessGoal(userProfile.getFitnessGoal());
        template.setWorkoutSplit(userProfile.getWorkoutSplit());

        Map<MovementPattern, List<Exercise>> movementMap = filteredExercises.stream()
                .collect(Collectors.groupingBy(Exercise::getMovementPattern));

        WorkoutGenerationStrategy strategy = strategyFactory.getStrategy(userProfile.getWorkoutSplit());
        List<PlanExercise> planExercises = strategy.generatePlan(movementMap, template, userProfile);

        template.setPlanExercises(planExercises);

        return template;
    }

    private List<Exercise> filterExercises(UserProfile userProfile) {
        return exerciseRepo.findAll().stream()
                .filter(exercise -> exercise.getTargetGoals().contains(userProfile.getFitnessGoal()))
                .filter(exercise -> exercise.getSuitableLevels().contains(userProfile.getFitnessLevel()))
                .filter(exercise -> !Collections.disjoint(exercise.getEquipmentType(), userProfile.getEquipmentType()))
                .toList();
    }

}
