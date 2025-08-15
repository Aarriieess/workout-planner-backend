package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutTemplateService {

    private final WorkoutTemplateRepo workoutTemplateRepo;
    private UserRepo userRepo;

    public WorkoutTemplateService(WorkoutTemplateRepo workoutTemplateRepo) {
        this.workoutTemplateRepo = workoutTemplateRepo;
    }

    public WorkoutTemplate getUserTemplate(Long userId) {
        return workoutTemplateRepo.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Workout template not found for user ID: " + userId));
    }

    public WorkoutTemplate createTemplate(WorkoutTemplate template) {
        return workoutTemplateRepo.save(template);
    }

    public WorkoutTemplate updateTemplate(Long id, WorkoutTemplate template) {
        WorkoutTemplate existingTemplate = workoutTemplateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout template not found"));

        existingTemplate.setName(template.getName());
        existingTemplate.setFitnessGoal(template.getFitnessGoal());

        return workoutTemplateRepo.save(existingTemplate);
    }

}
