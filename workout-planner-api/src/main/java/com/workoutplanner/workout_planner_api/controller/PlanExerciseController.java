package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.model.PlanExercise;
import com.workoutplanner.workout_planner_api.service.PlanExerciseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plan-exercises")
public class PlanExerciseController {

    private final PlanExerciseService planExerciseService;

    public PlanExerciseController(PlanExerciseService planExerciseService){
        this.planExerciseService = planExerciseService;
    }

    @GetMapping
    public List<PlanExercise> getAllPlanExercises(){
        return planExerciseService.getAllPlanExercises();
    }

    @GetMapping("/{id}")
    public PlanExercise getAllPlanExercisesById(@PathVariable Long id){
        return planExerciseService.getPlanExerciseById(id);
    }

    @PostMapping
    public PlanExercise createPlanExercise(@Valid @RequestBody PlanExercise planExercise){
        return planExerciseService.createPlanExercise(planExercise);
    }

    @PutMapping("/{id}")
    public PlanExercise updatePlanExercise(@PathVariable Long id, @Valid @RequestBody PlanExercise updated){
        return planExerciseService.updatePlanExercise(id, updated);
    }

    @DeleteMapping("/{id}")
    public void deletePlanExercise(@PathVariable Long id){
        planExerciseService.deletePlanExercise(id);
    }
}

