package com.workoutplanner.workout_planner_api.service.implementation;

import com.workoutplanner.workout_planner_api.auth.UserPrincipal;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;
import com.workoutplanner.workout_planner_api.mapper.WorkoutTemplateMapper;
import com.workoutplanner.workout_planner_api.model.*;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import com.workoutplanner.workout_planner_api.service.ExerciseService;
import com.workoutplanner.workout_planner_api.service.RuleBasedWorkoutService;
import com.workoutplanner.workout_planner_api.service.UserService;
import com.workoutplanner.workout_planner_api.service.WorkoutTemplateService;
import com.workoutplanner.workout_planner_api.service.strategy.WorkoutGenerationStrategy;
import com.workoutplanner.workout_planner_api.service.strategy.WorkoutStrategyFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class RuleBasedWorkoutServiceImpl implements RuleBasedWorkoutService {

    private final UserService userService;
    private final WorkoutTemplateService workoutTemplateService;
    private final WorkoutTemplateRepo workoutTemplateRepo;
    private final ExerciseService exerciseService;
    private final WorkoutStrategyFactory strategyFactory;
    private final WorkoutTemplateMapper workoutTemplateMapper;


    @Transactional
    @Override
    public WorkoutTemplateResponse generateTemplate(UserPrincipal user) {

        var userProfile = userService.getUserProfileEntity(user.getId());

        System.out.println("=== DEBUG USER PROFILE ===");
        System.out.println("User ID: " + user.getId());
        System.out.println("Training Days from DB: " + userProfile.getTrainingDays());
        System.out.println("Fitness Goal: " + userProfile.getFitnessGoal());
        System.out.println("========================");

        WorkoutSplit split = workoutTemplateService.determineWorkoutSplit(userProfile);

        System.out.println("🎯 Determined Split: " + split);

        WorkoutTemplate template = workoutTemplateService.createTemplate(
                userProfile.getUser(),
                userProfile,
                split
        );

        Map<MuscleGroup, List<Exercise>> exercisesByMuscleGroup
                = exerciseService.getExercisesByMuscleGroup(userProfile);

        WorkoutGenerationStrategy strategy = strategyFactory.getStrategy(split);

        System.out.println("🎯 Selected Split: " + split);
        System.out.println("🎯 Strategy Class: " + strategy.getClass().getSimpleName());

        List<PlanExercise> planExercises = strategy.generatePlan(userProfile, exercisesByMuscleGroup, template);

        workoutTemplateService.updateTemplateExercises(template, planExercises);

        WorkoutTemplate userTemplate = workoutTemplateRepo.save(template);

        return workoutTemplateMapper.toResponse(userTemplate);
    }
}
