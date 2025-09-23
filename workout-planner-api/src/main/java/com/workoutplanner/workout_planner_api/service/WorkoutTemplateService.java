package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.config.ResourceNotFoundException;
import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkoutTemplateService {

    private final WorkoutTemplateRepo workoutTemplateRepo;
    private final ExerciseRepo exerciseRepo;

    public WorkoutTemplateService(WorkoutTemplateRepo workoutTemplateRepo,
                                  ExerciseRepo exerciseRepo) {
        this.workoutTemplateRepo = workoutTemplateRepo;
        this.exerciseRepo = exerciseRepo;
    }

    public WorkoutTemplate getUserTemplate(Long userId) {
        return workoutTemplateRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout template not found for user ID: " + userId));
    }

    @Transactional
    public WorkoutTemplate updateTemplate(Long templateId, WorkoutTemplateRequest request, Long userId) {
        WorkoutTemplate template = workoutTemplateRepo.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout template not found"));

        ownershipTemplateCheck(template, userId);

        //updates the template info
        template.updateFromRequest(request);

        //clear the template exercises
        template.clearExercises();

        for (PlanExerciseRequest exerciseRequest : request.getPlanExerciseRequestList()) {
                exerciseRepo.findById(exerciseRequest.getExerciseId())
                            .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

                    PlanExercise planExercise = new PlanExercise();
                    planExercise.updateFromRequest(exerciseRequest);
                    planExercise.setWorkoutTemplate(template);

                    template.addExercise(planExercise);
                }

        return workoutTemplateRepo.save(template);
    }

    public WorkoutTemplate addExerciseToTemplate(Long templateId, PlanExerciseRequest request, Long userId){
        WorkoutTemplate template = workoutTemplateRepo.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        ownershipTemplateCheck(template, userId);

        Exercise exercise = exerciseRepo.findById(request.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        PlanExercise planExercise = PlanExercise.fromRequest(request, template, exercise);

        template.addExercise(planExercise);

        return workoutTemplateRepo.save(template);
    }

    public void removeExerciseFromTemplate(Long templateId, Long planExerciseId, Long userId) {
        WorkoutTemplate template = workoutTemplateRepo.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        ownershipTemplateCheck(template, userId);

        PlanExercise toRemove = template.getPlanExercises().stream()
                .filter(pe -> pe.getId().equals(planExerciseId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found in this template"));

        template.removeExercise(toRemove);

        workoutTemplateRepo.save(template);
    }

    private void ownershipTemplateCheck(WorkoutTemplate template, Long userId) {
        if (!template.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only modify your own template");
        }
    }
}
