package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.config.ResourceNotFoundException;
import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;
import com.workoutplanner.workout_planner_api.mapper.PlanExerciseMapper;
import com.workoutplanner.workout_planner_api.mapper.WorkoutTemplateMapper;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        WorkoutTemplate template = findTemplateForUser(templateId, userId);

        Exercise exercise = exerciseRepo.findById(planExerciseRequest.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

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
}
