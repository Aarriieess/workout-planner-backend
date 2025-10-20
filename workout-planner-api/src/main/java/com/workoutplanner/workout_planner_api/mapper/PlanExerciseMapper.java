package com.workoutplanner.workout_planner_api.mapper;


import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.PlanExerciseResponse;
import com.workoutplanner.workout_planner_api.model.PlanExercise;

public interface PlanExerciseMapper {
    PlanExerciseResponse toResponse(PlanExercise entity);
    PlanExercise toEntity(PlanExerciseRequest request);
}
