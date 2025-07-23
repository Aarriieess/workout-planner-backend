package com.workoutplanner.workout_planner_api.repo;

import com.workoutplanner.workout_planner_api.model.PlanExercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanExerciseRepo extends JpaRepository<PlanExercise, Long> {
}
