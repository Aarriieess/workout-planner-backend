package com.workoutplanner.workout_planner_api.repo;

import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.MuscleGroup;
import com.workoutplanner.workout_planner_api.model.WorkoutEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepo extends JpaRepository<Exercise, Long> {
    List<Exercise> findByWorkoutEnvironment(WorkoutEnvironment environment);

    @Query("""
    SELECT e FROM Exercise e
    WHERE e.primaryMuscleGroup = :group OR :group MEMBER OF e.secondaryMuscleGroup
""")
    Page<Exercise> findByMuscleGroup(MuscleGroup muscleGroup, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);
}
