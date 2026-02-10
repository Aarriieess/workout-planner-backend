package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.config.ResourceNotFoundException;
import com.workoutplanner.workout_planner_api.dto.ExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.ExerciseResponse;
import com.workoutplanner.workout_planner_api.mapper.ExerciseMapper;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.MuscleGroup;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import com.workoutplanner.workout_planner_api.model.WorkoutEnvironment;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepo exerciseRepo;
    private final ExerciseMapper exerciseMapper;


    public Page<ExerciseResponse> getExercise(int page, int size, MuscleGroup muscleGroup, WorkoutEnvironment environment, String search) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<Exercise> spec = ((root, query, cb) -> cb.conjunction());

        if (muscleGroup != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("primaryMuscleGroup"), muscleGroup)
            );
        }

        if (environment != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Exercise, WorkoutEnvironment> environmentJoin = root.join("workoutEnvironment");
                return cb.equal(environmentJoin, environment);
            });
        }

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%")
            );
        }

        Page<Exercise> exercises = exerciseRepo.findAll(spec, pageable);
        return exercises.map(exerciseMapper::toResponse);
    }

    public ExerciseResponse getExerciseById(Long id) {
        Exercise exercise = exerciseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        return exerciseMapper.toResponse(exercise);
    }

    public Map<MuscleGroup, List<Exercise>> getExercisesByMuscleGroup(UserProfile userProfile) {
        List<Exercise> filteredExercise = exerciseRepo.findByWorkoutEnvironment(userProfile.getWorkoutEnvironment());

        return filteredExercise.stream()
                .collect(Collectors.groupingBy(Exercise::getPrimaryMuscleGroup));
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

        exerciseMapper.updateEntityFromRequest(request, exercise);
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
