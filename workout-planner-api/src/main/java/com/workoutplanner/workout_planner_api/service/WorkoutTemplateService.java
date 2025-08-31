package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.repo.PlanExerciseRepo;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WorkoutTemplateService {

    private final WorkoutTemplateRepo workoutTemplateRepo;
    private final ExerciseRepo exerciseRepo;
    private final PlanExerciseRepo planExerciseRepo;

    public WorkoutTemplateService(WorkoutTemplateRepo workoutTemplateRepo,
                                  ExerciseRepo exerciseRepo,
                                  PlanExerciseRepo planExerciseRepo) {
        this.workoutTemplateRepo = workoutTemplateRepo;
        this.exerciseRepo = exerciseRepo;
        this.planExerciseRepo = planExerciseRepo;
    }

    public WorkoutTemplate getUserTemplate(Long userId) {
        return workoutTemplateRepo.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Workout template not found for user ID: " + userId));
    }

    @Transactional
    public WorkoutTemplate updateTemplate(Long templateId, WorkoutTemplateRequest request, Long userId) {
        WorkoutTemplate template = workoutTemplateRepo.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Workout template not found"));

        if (!template.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own template");
        }

        //updates the template info
        template.updateFromRequest(request);

        //clear the template exercises
        template.clearExercises();

        for (PlanExerciseRequest exerciseRequest : request.getPlanExerciseRequestList()) {
                Exercise exercise = exerciseRepo.findById(exerciseRequest.getExerciseId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));

                    PlanExercise planExercise = new PlanExercise();
                    planExercise.updateFromRequest(exerciseRequest);
                    planExercise.setWorkoutTemplate(template);

                    template.addExercise(planExercise);
                }

        return workoutTemplateRepo.save(template);
    }

    public WorkoutTemplate addExerciseToTemplate(Long templateId, PlanExerciseRequest request, Long userId){
        WorkoutTemplate template = workoutTemplateRepo.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (!template.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only modify your own template");
        }

        Exercise exercise = exerciseRepo.findById(request.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        PlanExercise planExercise = PlanExercise.fromRequest(request, template, exercise);

        template.addExercise(planExercise);

        return workoutTemplateRepo.save(template);
    }

    public WorkoutTemplate removeExerciseFromTemplate(Long templateId, Long planExerciseId) {
        WorkoutTemplate template = workoutTemplateRepo.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        PlanExercise toRemove = template.getPlanExercises().stream()
                .filter(pe -> pe.getId().equals(planExerciseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Exercise not found in this template"));

        template.removeExercise(toRemove);

        return workoutTemplateRepo.save(template);
    }
}
