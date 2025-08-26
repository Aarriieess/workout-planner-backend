package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.PlanExerciseResponse;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import com.workoutplanner.workout_planner_api.repo.PlanExerciseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanExerciseService {

    @Autowired
    private PlanExerciseRepo planExerciseRepo;

    public List<PlanExercise> getAllPlanExercises(){
        return planExerciseRepo.findAll();
    }

    public PlanExerciseResponse getPlanExerciseById(Long id){
        PlanExercise planExercise = planExerciseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PlanExercise not found"));

        return new PlanExerciseResponse(
                planExercise.getId(),
                planExercise.getExercise().getName(),
                planExercise.getSets(),
                planExercise.getReps(),
                planExercise.getRestSeconds()
        );
    }

    public PlanExercise createPlanExercise(PlanExercise planExercise){
        return planExerciseRepo.save(planExercise);
    }

    public PlanExercise updateExercise(Long planExerciseId, Long userId, PlanExerciseRequest request){

        PlanExercise existingExercise  = planExerciseRepo.findById(planExerciseId)
                .orElseThrow(() -> new RuntimeException("PlanExercise not found"));

        if (!existingExercise.getWorkoutTemplate().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: This exercise is not in your template");
        }

        existingExercise.setReps(request.getReps());
        existingExercise.setSets(request.getSets());
        existingExercise.setRestSeconds(request.getRestSeconds());

        return planExerciseRepo.save(existingExercise );
    }

    public void deleteExercise(Long id){
        if (!planExerciseRepo.existsById(id)){
            throw new RuntimeException("PlanExercise not found");
        }
        planExerciseRepo.deleteById(id);
    }
}
