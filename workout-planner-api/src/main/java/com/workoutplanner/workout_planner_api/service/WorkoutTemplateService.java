package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.repo.WorkoutTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class WorkoutTemplateService {

    private final WorkoutTemplateRepo workoutTemplateRepo;

    public WorkoutTemplateService(WorkoutTemplateRepo workoutTemplateRepo){
        this.workoutTemplateRepo = workoutTemplateRepo;
    }

    public List<WorkoutTemplate> getTemplates(FitnessGoal fitnessGoal, Long id){
        if (fitnessGoal != null && id != null){
            return workoutTemplateRepo.findByFitnessGoalAndId(fitnessGoal, id);
        } else if (fitnessGoal != null) {
            return workoutTemplateRepo.findByFitnessGoal(fitnessGoal);
        } else {
            return workoutTemplateRepo.findAll();
        }
    }

    public WorkoutTemplate getTemplateById(Long id){
        return workoutTemplateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout template not found"));
    }

    public WorkoutTemplate createTemplate(WorkoutTemplate template){
        return workoutTemplateRepo.save(template);
    }

    public WorkoutTemplate updateTemplate(Long id, WorkoutTemplate template) {
        WorkoutTemplate existingTemplate = workoutTemplateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout template not found"));

        existingTemplate.setName(template.getName());
        existingTemplate.setFitnessGoal(template.getFitnessGoal());

        return workoutTemplateRepo.save(existingTemplate);
    }

    public void deleteTemplate(Long id) {
        if (!workoutTemplateRepo.existsById(id)){
            throw new RuntimeException("Workout template now found");
        }

        workoutTemplateRepo.deleteById(id);
    }

}
