package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.PlanExerciseResponse;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;
import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.WorkoutSplit;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-17T19:10:16+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class WorkoutTemplateMapperImpl implements WorkoutTemplateMapper {

    @Override
    public WorkoutTemplateResponse toResponse(WorkoutTemplate workoutTemplate) {
        if ( workoutTemplate == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        FitnessGoal fitnessGoal = null;
        WorkoutSplit workoutSplit = null;

        id = workoutTemplate.getId();
        name = workoutTemplate.getName();
        fitnessGoal = workoutTemplate.getFitnessGoal();
        workoutSplit = workoutTemplate.getWorkoutSplit();

        List<PlanExerciseResponse> planExercise = null;

        WorkoutTemplateResponse workoutTemplateResponse = new WorkoutTemplateResponse( id, name, fitnessGoal, workoutSplit, planExercise );

        return workoutTemplateResponse;
    }

    @Override
    public WorkoutTemplate toEntity(WorkoutTemplateRequest request) {
        if ( request == null ) {
            return null;
        }

        WorkoutTemplate.WorkoutTemplateBuilder workoutTemplate = WorkoutTemplate.builder();

        workoutTemplate.name( request.getName() );
        workoutTemplate.workoutSplit( request.getWorkoutSplit() );
        workoutTemplate.fitnessGoal( request.getFitnessGoal() );

        return workoutTemplate.build();
    }

    @Override
    public void updateEntityFromRequest(WorkoutTemplateRequest request, WorkoutTemplate workoutTemplate) {
        if ( request == null ) {
            return;
        }

        workoutTemplate.setName( request.getName() );
        workoutTemplate.setWorkoutSplit( request.getWorkoutSplit() );
        workoutTemplate.setFitnessGoal( request.getFitnessGoal() );
    }
}
