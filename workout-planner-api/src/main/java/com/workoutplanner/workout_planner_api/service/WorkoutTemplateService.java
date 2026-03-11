package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.config.ResourceNotFoundException;
import com.workoutplanner.workout_planner_api.dto.*;
import com.workoutplanner.workout_planner_api.mapper.PlanExerciseMapper;
import com.workoutplanner.workout_planner_api.mapper.WorkoutTemplateMapper;
import com.workoutplanner.workout_planner_api.model.*;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import lombok.RequiredArgsConstructor;
import org.hibernate.jdbc.Work;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkoutTemplateService {

    private final WorkoutTemplateRepo workoutTemplateRepo;
    private final ExerciseRepo exerciseRepo;
    private final WorkoutTemplateMapper templateMapper;
    private final PlanExerciseMapper planExerciseMapper;
    private final UserRepo userRepo;


    public WorkoutTemplateResponse getUserTemplate(Long userId) {
        WorkoutTemplate template = workoutTemplateRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout template not found for user ID: " + userId));

        return templateMapper.toResponse(template);
    }

    public WorkoutTemplate createTemplate (
            User user,
            UserProfile profile,
            WorkoutSplit split
    ) {
        WorkoutTemplate template = workoutTemplateRepo.findByUser(user)
                .orElse(new WorkoutTemplate());

        template.setUser(user);
        template.setName("Smart Plan - " + profile.getFitnessGoal().name());
        template.setFitnessGoal(profile.getFitnessGoal());
        template.setWorkoutSplit(split);

        workoutTemplateRepo.save(template);
        return template;
    }

    public WorkoutTemplateResponse createEmptyTemplate (Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<WorkoutTemplate> existing = workoutTemplateRepo.findByUser(user);
        if(existing.isPresent()) {
            return templateMapper.toResponse(existing.get());
        }

        WorkoutTemplate template = new WorkoutTemplate();
        template.setUser(user);

        workoutTemplateRepo.save(template);

        return templateMapper.toResponse(template);
    }

    @Transactional
    public WorkoutTemplateResponse updateTemplate(
            WorkoutTemplateRequest request,
            Long userId
    ) {
        WorkoutTemplate template = findTemplateByUserId(userId);
        templateMapper.updateEntityFromRequest(request, template);

        List<PlanExercise> exercises = request.getPlanExerciseRequestList()
                        .stream().map(req -> {
                                    Exercise exercise = exerciseRepo.findById(req.getExerciseId())
                                            .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));

                                    PlanExercise planExercise = planExerciseMapper.toEntity(req);
                                    planExercise.setExercise(exercise);
                                    planExercise.setWorkoutTemplate(template);

                                    return planExercise;
                        }).toList();

        template.getPlanExercises().clear();
        template.getPlanExercises().addAll(exercises);

        return templateMapper.toResponse(workoutTemplateRepo.save(template));
    }

    public PlanExerciseResponse addExerciseToTemplateWithDefaults(Long userId, Long exerciseId) {
        WorkoutTemplate template = findTemplateByUserId(userId);

        Exercise exercise = exerciseRepo.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        PlanExercise planExercise = template.addDefaultPlanExercise(exercise);

        workoutTemplateRepo.save(template);

        return planExerciseMapper.toResponse(planExercise);
    }

    public void removeExerciseFromTemplate(
            Long planExerciseId,
            Long userId
    ) {
        WorkoutTemplate template = findTemplateByUserId(userId);

        PlanExercise toRemove = template.getPlanExercises().stream()
                .filter(pe -> pe.getId().equals(planExerciseId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found in this template"));

        template.removeExercise(toRemove);
        workoutTemplateRepo.save(template);
    }

    private WorkoutTemplate findTemplateByUserId(Long userId) {
        WorkoutTemplate template = workoutTemplateRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        if (!template.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only modify your own template");
        }

        return template;
    }

    public WorkoutSplit determineWorkoutSplit (UserProfile profile) {
        int days = profile.getTrainingDays();

        return switch (days) {
            case 2, 4 -> WorkoutSplit.UPPER_LOWER;
            case 5, 7 -> WorkoutSplit.BRO_SPLIT;
            case 3, 6 -> WorkoutSplit.PPL;
            case 1 -> WorkoutSplit.FULL_BODY;
            default -> WorkoutSplit.FULL_BODY;
        };
    }

    @Transactional
    public void updateWholeTemplateExercises(WorkoutTemplate template, List<PlanExercise> planExercises) {
        template.getPlanExercises().clear();

        planExercises.forEach(exercise -> exercise.setWorkoutTemplate(template));
        template.getPlanExercises().addAll(planExercises);
    }

    @Transactional
    public PlanExerciseResponse updatePlanExercise(Long userId, PlaneExerciseUpdateRequest request) {
        WorkoutTemplate template = findTemplateByUserId(userId);

        PlanExercise planExercise = template.getPlanExercises()
                        .stream()
                        .filter(ex -> ex.getId().equals(request.getPlanExerciseId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));

        planExercise.setId(request.getPlanExerciseId());
        planExercise.setSets(request.getSets());
        planExercise.setReps(request.getReps());
        planExercise.setRestSeconds(request.getRestSeconds());

        return planExerciseMapper.toResponse(planExercise);
    }
}
