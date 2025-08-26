package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkoutTemplateService {

    private final WorkoutTemplateRepo workoutTemplateRepo;
    private final ExerciseRepo exerciseRepo;

    public WorkoutTemplateService(WorkoutTemplateRepo workoutTemplateRepo, ExerciseRepo exerciseRepo) {
        this.workoutTemplateRepo = workoutTemplateRepo;
        this.exerciseRepo = exerciseRepo;
    }

    public WorkoutTemplate getUserTemplate(Long userId) {
        return workoutTemplateRepo.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Workout template not found for user ID: " + userId));
    }

    @Transactional
    public void updateTemplate(Long templateId, WorkoutTemplateRequest request) {
        WorkoutTemplate template = workoutTemplateRepo.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Workout template not found"));

        template.setName(request.getName());
        template.setFitnessGoal(request.getFitnessGoal());
        template.setWorkoutSplit(request.getWorkoutSplit());

        template.clearExercises();

        for (PlanExerciseRequest exerciseRequest : request.getPlanExerciseRequestList()) {
            Exercise exercise = exerciseRepo.findById(exerciseRequest.getExerciseId())
                    .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

            PlanExercise planExercise = new PlanExercise();
            planExercise.setExercise(exercise);
            planExercise.setSets(exerciseRequest.getSets());
            planExercise.setReps(exerciseRequest.getReps());
            planExercise.setRestSeconds(exerciseRequest.getRestSeconds());

            template.addExercise(planExercise);
        }

        workoutTemplateRepo.save(template);
    }

}
