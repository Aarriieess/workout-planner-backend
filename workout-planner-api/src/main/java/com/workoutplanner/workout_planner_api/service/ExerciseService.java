package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepo exerciseRepo;

    public List<Exercise> getAllExercise() {
        return exerciseRepo.findAll();
    }

    public Exercise getExerciseById(Long id){
        return exerciseRepo.findById(id)
                .orElse(null);
    }

    public Exercise createExercise(Exercise exercise){
        return exerciseRepo.save(exercise);
    }

    public Exercise updateExercise(Long id, Exercise exercise) {
        Exercise existingExercise = exerciseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        existingExercise.setName(exercise.getName());
        existingExercise.setPrimaryMuscleGroup(exercise.getPrimaryMuscleGroup());
        existingExercise.setSecondaryMuscleGroup(exercise.getSecondaryMuscleGroup());
        existingExercise.setDifficulty(exercise.getDifficulty());
        existingExercise.setDescription(exercise.getDescription());
        existingExercise.setExerciseType(exercise.getExerciseType());
        existingExercise.setMovementPattern(exercise.getMovementPattern());
        existingExercise.setTargetGoals(exercise.getTargetGoals());
        existingExercise.setSuitableLevels(exercise.getSuitableLevels());
        existingExercise.setEquipmentType(exercise.getEquipmentType());
        existingExercise.setUnilateral(exercise.isUnilateral());

        return exerciseRepo.save(existingExercise);
    }

    public void deleteExercise(Long id) {
        if (!exerciseRepo.existsById(id)){
            throw new RuntimeException("Workout template now found");
        }

        exerciseRepo.deleteById(id);
    }
}
