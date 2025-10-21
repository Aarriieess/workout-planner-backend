package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.ExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.ExerciseResponse;
import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ExerciseMapper {

    ExerciseResponse toResponse(Exercise exercise);
    Exercise toEntity(ExerciseRequest request);

    void updateEntityFromRequest(
            ExerciseRequest request,
            @MappingTarget Exercise exercise
    );
}
