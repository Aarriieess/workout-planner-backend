package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.dto.ExerciseRequest;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.MuscleGroup;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ExerciseService {

    private final ExerciseRepo exerciseRepo;

    public ExerciseService(ExerciseRepo exerciseRepo) {
        this.exerciseRepo = exerciseRepo;
    }

    public Page<Exercise> getExercise(MuscleGroup muscleGroup, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        if (muscleGroup != null) {
            return exerciseRepo.findByMuscleGroup(muscleGroup, pageable);
        } else {
            return exerciseRepo.findAllExercises(pageable);
        }
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
