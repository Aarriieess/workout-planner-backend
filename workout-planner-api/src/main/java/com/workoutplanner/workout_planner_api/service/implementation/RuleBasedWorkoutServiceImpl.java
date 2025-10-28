package com.workoutplanner.workout_planner_api.service.implementation;

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
    private final UserRepo userRepo;
    private final WorkoutTemplateService workoutTemplateService;
    private final WorkoutTemplateRepo workoutTemplateRepo;
    private final ExerciseService exerciseService;
    private final WorkoutStrategyFactory strategyFactory;
    private final WorkoutTemplateMapper workoutTemplateMapper;


    @Transactional
    @Override
    public WorkoutTemplateResponse generateTemplate(@Valid UserProfileRequest request) {
        userService.validateRequest(request);
        UserProfile profile = userService.createUserProfile(request);

        WorkoutSplit split = workoutTemplateService.determineWorkoutSplit(profile);
        WorkoutTemplate template = workoutTemplateService.createTemplate(profile.getUser(), profile, split);

        Map<MuscleGroup, List<Exercise>> exercisesByMuscleGroup = exerciseService.getExercisesByMuscleGroup(profile);
        WorkoutGenerationStrategy strategy = strategyFactory.getStrategy(split);
        List<PlanExercise> planExercises = strategy.generatePlan(profile, exercisesByMuscleGroup, template);

        workoutTemplateService.updateTemplateExercises(template, planExercises);
        workoutTemplateRepo.save(template);

        return workoutTemplateMapper.toResponse(template);
    }
}
