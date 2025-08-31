package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.dto.ExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.ExerciseResponse;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepo exerciseRepo;

    public ExerciseService(ExerciseRepo exerciseRepo) {
        this.exerciseRepo = exerciseRepo;
    }

    public List<Exercise> getAllExercise() {
        return exerciseRepo.findAll();
    }

    public Exercise getExerciseById(Long id){
        return exerciseRepo.findById(id)
                .orElse(null);
    }

    public Exercise createExercise(ExerciseRequest request){
        return exerciseRepo.save(Exercise.fromRequest(request));
    }

    public void updateExercise(Long id, ExerciseRequest request) {
        Exercise exercise = exerciseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));
        exercise.updateFromRequest(request);
        exerciseRepo.save(exercise);
    }

    public void deleteExercise(Long id) {
        if (!exerciseRepo.existsById(id)){
            throw new RuntimeException("Workout template now found");
        }

        exerciseRepo.deleteById(id);
    }
}
