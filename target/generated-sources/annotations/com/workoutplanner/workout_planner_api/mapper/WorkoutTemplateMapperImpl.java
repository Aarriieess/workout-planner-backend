package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.PlanExerciseResponse;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;
import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import com.workoutplanner.workout_planner_api.model.WorkoutSplit;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T15:17:12+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class WorkoutTemplateMapperImpl implements WorkoutTemplateMapper {

    @Autowired
    private PlanExerciseMapper planExerciseMapper;

    @Override
    public WorkoutTemplateResponse toResponse(WorkoutTemplate workoutTemplate) {
        if ( workoutTemplate == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        FitnessGoal fitnessGoal = null;
        WorkoutSplit workoutSplit = null;
        List<PlanExerciseResponse> planExercises = null;

        id = workoutTemplate.getId();
        name = workoutTemplate.getName();
        fitnessGoal = workoutTemplate.getFitnessGoal();
        workoutSplit = workoutTemplate.getWorkoutSplit();
        planExercises = planExerciseListToPlanExerciseResponseList( workoutTemplate.getPlanExercises() );

        WorkoutTemplateResponse workoutTemplateResponse = new WorkoutTemplateResponse( id, name, fitnessGoal, workoutSplit, planExercises );

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

    protected List<PlanExerciseResponse> planExerciseListToPlanExerciseResponseList(List<PlanExercise> list) {
        if ( list == null ) {
            return null;
        }

        List<PlanExerciseResponse> list1 = new ArrayList<PlanExerciseResponse>( list.size() );
        for ( PlanExercise planExercise : list ) {
            list1.add( planExerciseMapper.toResponse( planExercise ) );
        }

        return list1;
    }
}
