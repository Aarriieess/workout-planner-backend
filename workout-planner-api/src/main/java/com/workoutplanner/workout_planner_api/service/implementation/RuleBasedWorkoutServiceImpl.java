package com.workoutplanner.workout_planner_api.service.implementation;

import com.workoutplanner.workout_planner_api.auth.UserPrincipal;
import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;
import com.workoutplanner.workout_planner_api.mapper.WorkoutTemplateMapper;
import com.workoutplanner.workout_planner_api.model.*;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import com.workoutplanner.workout_planner_api.service.ExerciseService;
import com.workoutplanner.workout_planner_api.service.RuleBasedWorkoutService;
import com.workoutplanner.workout_planner_api.service.UserService;
import com.workoutplanner.workout_planner_api.service.WorkoutTemplateService;
import com.workoutplanner.workout_planner_api.service.strategy.WorkoutGenerationStrategy;
import com.workoutplanner.workout_planner_api.service.strategy.WorkoutStrategyFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


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

        WorkoutSplit split = workoutTemplateService.determineWorkoutSplit(userProfile);

        WorkoutTemplate template = workoutTemplateService.createTemplate(
                userProfile.getUser(),
                userProfile,
                split
        );

        Map<MuscleGroup, List<Exercise>> exercisesByMuscleGroup
                = exerciseService.getExercisesByMuscleGroup(userProfile);

        WorkoutGenerationStrategy strategy = strategyFactory.getStrategy(split);

        List<PlanExercise> planExercises = strategy.generatePlan(userProfile, exercisesByMuscleGroup, template);

        workoutTemplateService.updateTemplateExercises(template, planExercises);

        workoutTemplateRepo.save(template);

        return workoutTemplateMapper.toResponse(template);
    }
}
