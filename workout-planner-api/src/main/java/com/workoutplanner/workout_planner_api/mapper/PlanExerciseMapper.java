package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.PlanExerciseResponse;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ExerciseMapper.class}
)
public interface PlanExerciseMapper {

    PlanExerciseResponse toResponse(PlanExercise planExercise);
    PlanExercise toEntity(PlanExerciseRequest request);

    void updateEntityFromRequest(
            PlanExerciseRequest request,
            @MappingTarget PlanExercise planExercise
    );
}
