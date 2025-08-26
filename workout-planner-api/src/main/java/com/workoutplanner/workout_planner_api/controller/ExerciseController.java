package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.dto.ExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.ExerciseResponse;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseRepo exerciseRepo;

    public ExerciseController(ExerciseService exerciseService, ExerciseRepo exerciseRepo){
        this.exerciseService = exerciseService;
        this.exerciseRepo = exerciseRepo;
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponse>> getAllExercise() {
        List<ExerciseResponse> responses = exerciseService.getAllExercise()
                .stream()
                .map(ExerciseResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getExerciseById(@PathVariable Long id){
        Exercise exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(ExerciseResponse.fromEntity(exercise));
    }

    @PostMapping
    public Exercise createExercise(@RequestBody ExerciseRequest exercise){
        return exerciseService.createExercise(exercise);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponse> updateExercise(@PathVariable Long id,
                                         @Valid @RequestBody ExerciseRequest request){

        exerciseService.updateExercise(id, request);
        Exercise updated = exerciseRepo.findById(id).orElseThrow();
        return ResponseEntity.ok(ExerciseResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public void deleteExercise(@PathVariable Long id){
        exerciseService.deleteExercise(id);
    }

}
