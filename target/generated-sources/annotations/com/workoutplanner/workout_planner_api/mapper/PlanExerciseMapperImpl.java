package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.PlanExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.PlanExerciseResponse;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.PlanExercise;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-04T18:42:19+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class PlanExerciseMapperImpl implements PlanExerciseMapper {

    @Override
    public PlanExerciseResponse toResponse(PlanExercise planExercise) {
        if ( planExercise == null ) {
            return null;
        }

        Long planExerciseId = null;
        Long exerciseId = null;
        String exerciseName = null;
        int sets = 0;
        int reps = 0;
        int restSeconds = 0;
        int dayIndex = 0;
        int orderIndex = 0;

        planExerciseId = planExercise.getId();
        exerciseId = planExerciseExerciseId( planExercise );
        exerciseName = planExerciseExerciseName( planExercise );
        sets = planExercise.getSets();
        reps = planExercise.getReps();
        restSeconds = planExercise.getRestSeconds();
        dayIndex = planExercise.getDayIndex();
        orderIndex = planExercise.getOrderIndex();

        PlanExerciseResponse planExerciseResponse = new PlanExerciseResponse( planExerciseId, exerciseId, exerciseName, sets, reps, restSeconds, dayIndex, orderIndex );

        return planExerciseResponse;
    }

    @Override
    public PlanExercise toEntity(PlanExerciseRequest request) {
        if ( request == null ) {
            return null;
        }

        PlanExercise.PlanExerciseBuilder planExercise = PlanExercise.builder();

        if ( request.getSets() != null ) {
            planExercise.sets( request.getSets() );
        }
        if ( request.getReps() != null ) {
            planExercise.reps( request.getReps() );
        }
        if ( request.getRestSeconds() != null ) {
            planExercise.restSeconds( request.getRestSeconds() );
        }

        return planExercise.build();
    }

    @Override
    public void updateEntityFromRequest(PlanExerciseRequest request, PlanExercise planExercise) {
        if ( request == null ) {
            return;
        }

        if ( request.getSets() != null ) {
            planExercise.setSets( request.getSets() );
        }
        if ( request.getReps() != null ) {
            planExercise.setReps( request.getReps() );
        }
        if ( request.getRestSeconds() != null ) {
            planExercise.setRestSeconds( request.getRestSeconds() );
        }
    }

    private Long planExerciseExerciseId(PlanExercise planExercise) {
        Exercise exercise = planExercise.getExercise();
        if ( exercise == null ) {
            return null;
        }
        return exercise.getId();
    }

    private String planExerciseExerciseName(PlanExercise planExercise) {
        Exercise exercise = planExercise.getExercise();
        if ( exercise == null ) {
            return null;
        }
        return exercise.getName();
    }
}
