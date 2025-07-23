package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.service.ExerciseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService){
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public List<Exercise> getAllExercise() {
        return exerciseService.getAllExercise();
    }

    @GetMapping("/{id}")
    public Exercise getExerciseById(@PathVariable Long id){
        return exerciseService.getExerciseById(id);
    }

    @PostMapping
    public Exercise createExercise(@RequestBody Exercise exercise){
        return exerciseService.createExercise(exercise);
    }

    @PutMapping("/{id}")
    public Exercise updateExercise(@PathVariable Long id, @RequestBody Exercise exercise){
        return exerciseService.updateExercise(id, exercise);
    }

    @DeleteMapping("/{id}")
    public void deleteExercise(@PathVariable Long id){
        exerciseService.deleteExercise(id);
    }

}
