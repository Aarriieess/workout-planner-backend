package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.dto.ExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.ExerciseResponse;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.MuscleGroup;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import com.workoutplanner.workout_planner_api.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Page<ExerciseResponse>> getAllExercises(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) MuscleGroup muscleGroup
    ) {
        Page<Exercise> exercises = exerciseService.getExercise(muscleGroup, page, size);
        return ResponseEntity.ok(exercises.map(ExerciseResponse::fromEntity));
    }


    @PostMapping
    public ResponseEntity<ExerciseResponse> createExercise(@Valid @RequestBody ExerciseRequest request) {
        Exercise exercise = exerciseService.createExercise(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ExerciseResponse.fromEntity(exercise));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponse> updateExercise(
            @PathVariable Long id,
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
