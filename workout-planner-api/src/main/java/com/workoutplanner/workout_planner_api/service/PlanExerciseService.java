package com.workoutplanner.workout_planner_api.service;

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

    public PlanExercise getPlanExerciseById(Long id){
        return planExerciseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PlanExercise not found"));
    }

    public PlanExercise createPlanExercise(PlanExercise planExercise){
        return planExerciseRepo.save(planExercise);
    }

    public PlanExercise updatePlanExercise(Long id, PlanExercise updated){
        PlanExercise existing  = planExerciseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PlanExercise not found"));

        existing .setReps(updated.getReps());
        existing .setSets(updated.getSets());
        existing .setRestSeconds(updated.getRestSeconds());


        return planExerciseRepo.save(existing );
    }

    public void deletePlanExercise(Long id){
        if (!planExerciseRepo.existsById(id)){
            throw new RuntimeException("PlanExercise not found");
        }
        planExerciseRepo.deleteById(id);
    }
}
