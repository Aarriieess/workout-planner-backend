package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.config.ResourceNotFoundException;
import com.workoutplanner.workout_planner_api.dto.ExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.ExerciseResponse;
import com.workoutplanner.workout_planner_api.mapper.ExerciseMapper;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.MuscleGroup;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepo exerciseRepo;
    private final ExerciseMapper exerciseMapper;


    public Page<ExerciseResponse> getExercise(MuscleGroup muscleGroup, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Exercise> exercisePage = (muscleGroup != null)
                ? exerciseRepo.findByMuscleGroup(muscleGroup, pageable)
                : exerciseRepo.findAll(pageable);

        return exercisePage.map(exerciseMapper::toResponse);
    }

    public ExerciseResponse createExercise(ExerciseRequest request){
        if (exerciseRepo.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Exercise with this name already exists");
        }

        Exercise exercise = exerciseMapper.toEntity(request);
        exerciseRepo.save(exercise);

        return exerciseMapper.toResponse(exercise);
    }

    public ExerciseResponse updateExercise(Long id, ExerciseRequest request) {
        Exercise exercise = exerciseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        exercise.updateFromRequest(request);
        Exercise updatedExercise = exerciseRepo.save(exercise);

        return exerciseMapper.toResponse(updatedExercise);

    }

    public void deleteExercise(Long id) {
        if (!exerciseRepo.existsById(id)){
            throw new RuntimeException("Workout template now found");
        }

        exerciseRepo.deleteById(id);
    }
}
