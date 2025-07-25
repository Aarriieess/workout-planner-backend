package com.workoutplanner.workout_planner_api.service.implementation;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.model.*;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import com.workoutplanner.workout_planner_api.service.RuleBasedWorkoutService;
import com.workoutplanner.workout_planner_api.service.strategy.WorkoutGenerationStrategy;
import com.workoutplanner.workout_planner_api.service.strategy.WorkoutStrategyFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RuleBasedWorkoutServiceImpl implements RuleBasedWorkoutService {

    private final ExerciseRepo exerciseRepo;
    private final WorkoutStrategyFactory strategyFactory;
    private final WorkoutTemplateRepo workoutTemplateRepo;
    private final UserRepo userRepo;

    public RuleBasedWorkoutServiceImpl(ExerciseRepo exerciseRepo, WorkoutStrategyFactory strategyFactory, WorkoutTemplateRepo workoutTemplateRepo, UserRepo userRepo) {
        this.exerciseRepo = exerciseRepo;
        this.strategyFactory = strategyFactory;
        this.workoutTemplateRepo = workoutTemplateRepo;
        this.userRepo = userRepo;
}

    @Override
    public WorkoutTemplate generateTemplate(UserProfileRequest request) {

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserProfile profile = new UserProfile(
                request.getFitnessLevel(),
                request.getFitnessGoal(),
                request.getEquipmentType(),
                request.getWorkoutSplit(),
                request.getTrainingDays()
        );

        WorkoutTemplate template = new WorkoutTemplate();
        template.setUser(user);
        template.setName("Smart Plan - " + profile.getFitnessGoal().name());
        template.setFitnessGoal(profile.getFitnessGoal());
        template.setWorkoutSplit(profile.getWorkoutSplit());

        List<Exercise> filteredExercises = filterExercises(profile);

        Map<MovementPattern, List<Exercise>> movementMap = filteredExercises.stream()
                .collect(Collectors.groupingBy(Exercise::getMovementPattern));

        WorkoutGenerationStrategy strategy = strategyFactory.getStrategy(profile.getWorkoutSplit());
        List<PlanExercise> planExercises = strategy.generatePlan(profile, movementMap, template);

        template.setPlanExercises(planExercises);

        return workoutTemplateRepo.save(template);
    }

    private List<Exercise> filterExercises(UserProfile userProfile) {
        return exerciseRepo.findAll().stream()
                .filter(exercise -> exercise.getTargetGoals().contains(userProfile.getFitnessGoal()))
                .filter(exercise -> exercise.getSuitableLevels().contains(userProfile.getFitnessLevel()))
                .filter(exercise -> !Collections.disjoint(exercise.getEquipmentType(), userProfile.getEquipmentType()))
                .toList();
    }

}
