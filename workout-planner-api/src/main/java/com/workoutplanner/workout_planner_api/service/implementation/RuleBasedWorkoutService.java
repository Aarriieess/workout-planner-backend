package com.workoutplanner.workout_planner_api.service.implementation;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.model.*;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import com.workoutplanner.workout_planner_api.service.strategy.WorkoutGenerationStrategy;
import com.workoutplanner.workout_planner_api.service.strategy.WorkoutStrategyFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RuleBasedWorkoutService implements com.workoutplanner.workout_planner_api.service.RuleBasedWorkoutService {

    private final ExerciseRepo exerciseRepo;
    private final WorkoutStrategyFactory strategyFactory;
    private final WorkoutTemplateRepo workoutTemplateRepo;
    private final UserRepo userRepo;

    public RuleBasedWorkoutService(ExerciseRepo exerciseRepo, WorkoutStrategyFactory strategyFactory, WorkoutTemplateRepo workoutTemplateRepo, UserRepo userRepo) {
        this.exerciseRepo = exerciseRepo;
        this.strategyFactory = strategyFactory;
        this.workoutTemplateRepo = workoutTemplateRepo;
        this.userRepo = userRepo;
}

    @Transactional
    @Override
    public WorkoutTemplate generateTemplate(@Valid UserProfileRequest request) {

        validateRequest(request);
        User user = findUserById(request.getUserId());
        UserProfile profile = createUserProfile(request);
        WorkoutSplit split = determineWorkoutSplit(profile);
        WorkoutTemplate template = getOrCreateTemplate(user, profile, split);

        Map<MuscleGroup, List<Exercise>> exercisesByMuscleGroup = getExercisesByMuscleGroup(profile);

        WorkoutGenerationStrategy strategy = strategyFactory.getStrategy(split);
        List<PlanExercise> planExercises = strategy.generatePlan(profile, exercisesByMuscleGroup , template);
        updateTemplateExercises(template, planExercises);

        return workoutTemplateRepo.save(template);
    }

    private void validateRequest(UserProfileRequest request) {

        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (request.getTrainingDays() < 1 || request.getTrainingDays() > 7) {
            throw new IllegalArgumentException("Training days must be between 1 and 6");
        }
    }

    private User findUserById(Long userId) {

        return userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private UserProfile createUserProfile (UserProfileRequest request) {
        return new UserProfile(
                request.getFitnessLevel(),
                request.getFitnessGoal(),
                request.getWorkoutEnvironment(),
                request.getTrainingDays()
        );
    }

    private WorkoutTemplate getOrCreateTemplate (User user, UserProfile profile, WorkoutSplit split) {

        WorkoutTemplate template = workoutTemplateRepo.findByUser(user)
                .orElse(new WorkoutTemplate());

        template.setUser(user);
        template.setName("Smart Plan - " + profile.getFitnessGoal().name());
        template.setFitnessGoal(profile.getFitnessGoal());
        template.setWorkoutSplit(split);

        return template;
    }

    private Map<MuscleGroup, List<Exercise>> getExercisesByMuscleGroup(UserProfile userProfile) {

        List<Exercise> filteredExercise = exerciseRepo.findByWorkoutEnvironment(userProfile.getWorkoutEnvironment());

        return filteredExercise.stream()
                .collect(Collectors.groupingBy(Exercise::getPrimaryMuscleGroup));
    }

    private WorkoutSplit determineWorkoutSplit (UserProfile profile) {

        int days = profile.getTrainingDays();

        return switch (days) {
            case 2, 4 -> WorkoutSplit.UPPER_LOWER;
            case 5 -> WorkoutSplit.BRO_SPLIT;
            case 3, 6 -> WorkoutSplit.PPL;
            default -> WorkoutSplit.FULL_BODY;
        };
    }

    private void updateTemplateExercises (WorkoutTemplate template, List<PlanExercise> planExercises) {

        template.getPlanExercises().clear();

        planExercises.forEach(exercise -> exercise.setWorkoutTemplate(template));
        template.getPlanExercises().addAll(planExercises);
    }

}
