package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.PlanExerciseResponse;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ExerciseMapper.class}
)
public interface PlanExerciseMapper {

    PlanExerciseResponse toResponse(PlanExercise planExercise);
    PlanExercise toEntity(PlanExerciseRequest request);
}
