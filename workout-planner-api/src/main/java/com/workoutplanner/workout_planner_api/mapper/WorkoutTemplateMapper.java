package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PlanExerciseMapper.class}
)
public interface WorkoutTemplateMapper {

    WorkoutTemplateResponse toResponse(WorkoutTemplate workoutTemplate);
    WorkoutTemplate toEntity(WorkoutTemplateRequest request);
}
