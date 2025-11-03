package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.config.ResourceNotFoundException;
import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;
import com.workoutplanner.workout_planner_api.mapper.PlanExerciseMapper;
import com.workoutplanner.workout_planner_api.mapper.WorkoutTemplateMapper;
import com.workoutplanner.workout_planner_api.model.*;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutTemplateService {

    private final WorkoutTemplateRepo workoutTemplateRepo;
    private final ExerciseRepo exerciseRepo;
    private final WorkoutTemplateMapper templateMapper;
    private final PlanExerciseMapper planExerciseMapper;


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

        return template;
    }

    @Transactional
    public WorkoutTemplateResponse updateTemplate(
            Long templateId,
            WorkoutTemplateRequest request,
            Long userId
    ) {
        WorkoutTemplate template = findTemplateForUser(templateId, userId);
        template.clearExercises();

        for (PlanExerciseRequest planExerciseRequest : request.getPlanExerciseRequestList()) {
            Exercise exercise = exerciseRepo.findById(planExerciseRequest.getExerciseId())
                            .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

                    PlanExercise planExercise = planExerciseMapper.toEntity(planExerciseRequest);
                    planExercise.setExercise(exercise);
                    template.addExercise(planExercise);
                }

        templateMapper.updateEntityFromRequest(request, template);
        workoutTemplateRepo.save(template);

        return templateMapper.toResponse(template);
    }

    public WorkoutTemplateResponse addExerciseToTemplate(
            Long templateId,
            PlanExerciseRequest planExerciseRequest,
            Long userId
    ){
        WorkoutTemplate template = workoutTemplateRepo.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));


        Exercise exercise = exerciseRepo.findById(planExerciseRequest.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        boolean alreadyAdded = template.getPlanExercises().stream()
                .anyMatch(pe -> pe.getExercise().getId().equals(exercise.getId()));

        if (alreadyAdded) {
            throw new IllegalStateException("Exercise already exists in this template.");
        }

        PlanExercise planExercise = planExerciseMapper.toEntity(planExerciseRequest);
        planExercise.setExercise(exercise);

        template.addExercise(planExercise);

        workoutTemplateRepo.save(template);
        return templateMapper.toResponse(template);
    }

    public void removeExerciseFromTemplate(
            Long templateId,
            Long planExerciseId,
            Long userId
    ) {
        WorkoutTemplate template = findTemplateForUser(templateId, userId);

        PlanExercise toRemove = template.getPlanExercises().stream()
                .filter(pe -> pe.getId().equals(planExerciseId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found in this template"));

        template.removeExercise(toRemove);
        workoutTemplateRepo.save(template);
    }

    private WorkoutTemplate findTemplateForUser(Long templateId, Long userId) {
        WorkoutTemplate template = workoutTemplateRepo.findById(templateId)
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
            case 5 -> WorkoutSplit.BRO_SPLIT;
            case 3, 6 -> WorkoutSplit.PPL;
            default -> WorkoutSplit.FULL_BODY;
        };
    }

    public void updateTemplateExercises (WorkoutTemplate template, List<PlanExercise> planExercises) {
        template.getPlanExercises().clear();

        planExercises.forEach(exercise -> exercise.setWorkoutTemplate(template));
        template.getPlanExercises().addAll(planExercises);
    }
}
