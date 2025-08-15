package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import com.workoutplanner.workout_planner_api.service.PlanExerciseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@ControllerAdvice
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

    @PutMapping("/{userId}/plan-exercise/{id}")
    public PlanExercise updatePlanExercise(@PathVariable Long userId,
                                           @PathVariable Long id,
                                           @Valid @RequestBody PlanExerciseRequest request)  {
        return planExerciseService.updateExercise(id, userId, request);
    }

    @DeleteMapping("/{userId}/plan-exercise/{id}")
    public void deletePlanExercise(@PathVariable Long id){
        planExerciseService.deleteExercise(id);
    }

}

