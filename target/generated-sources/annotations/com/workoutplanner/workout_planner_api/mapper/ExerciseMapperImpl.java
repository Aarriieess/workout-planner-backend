package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.ExerciseRequest;
import com.workoutplanner.workout_planner_api.dto.ExerciseResponse;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.FitnessLevel;
import com.workoutplanner.workout_planner_api.model.MuscleGroup;
import com.workoutplanner.workout_planner_api.model.WorkoutEnvironment;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-20T12:49:39+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class ExerciseMapperImpl implements ExerciseMapper {

    @Override
    public ExerciseResponse toResponse(Exercise exercise) {
        if ( exercise == null ) {
            return null;
        }

        ExerciseResponse.ExerciseResponseBuilder exerciseResponse = ExerciseResponse.builder();

        exerciseResponse.id( exercise.getId() );
        exerciseResponse.name( exercise.getName() );
        exerciseResponse.primaryMuscleGroup( exercise.getPrimaryMuscleGroup() );
        List<MuscleGroup> list = exercise.getSecondaryMuscleGroup();
        if ( list != null ) {
            exerciseResponse.secondaryMuscleGroup( new ArrayList<MuscleGroup>( list ) );
        }
        exerciseResponse.description( exercise.getDescription() );
        List<FitnessGoal> list1 = exercise.getTargetGoals();
        if ( list1 != null ) {
            exerciseResponse.targetGoals( new ArrayList<FitnessGoal>( list1 ) );
        }
        List<FitnessLevel> list2 = exercise.getSuitableLevels();
        if ( list2 != null ) {
            exerciseResponse.suitableLevels( new ArrayList<FitnessLevel>( list2 ) );
        }
        List<WorkoutEnvironment> list3 = exercise.getWorkoutEnvironment();
        if ( list3 != null ) {
            exerciseResponse.workoutEnvironment( new ArrayList<WorkoutEnvironment>( list3 ) );
        }
        exerciseResponse.exerciseType( exercise.getExerciseType() );

        return exerciseResponse.build();
    }

    @Override
    public Exercise toEntity(ExerciseRequest request) {
        if ( request == null ) {
            return null;
        }

        Exercise.ExerciseBuilder exercise = Exercise.builder();

        exercise.name( request.getName() );
        exercise.primaryMuscleGroup( request.getPrimaryMuscleGroup() );
        List<MuscleGroup> list = request.getSecondaryMuscleGroup();
        if ( list != null ) {
            exercise.secondaryMuscleGroup( new ArrayList<MuscleGroup>( list ) );
        }
        exercise.description( request.getDescription() );
        List<FitnessGoal> list1 = request.getTargetGoals();
        if ( list1 != null ) {
            exercise.targetGoals( new ArrayList<FitnessGoal>( list1 ) );
        }
        List<FitnessLevel> list2 = request.getSuitableLevels();
        if ( list2 != null ) {
            exercise.suitableLevels( new ArrayList<FitnessLevel>( list2 ) );
        }
        List<WorkoutEnvironment> list3 = request.getWorkoutEnvironment();
        if ( list3 != null ) {
            exercise.workoutEnvironment( new ArrayList<WorkoutEnvironment>( list3 ) );
        }
        exercise.exerciseType( request.getExerciseType() );

        return exercise.build();
    }
}
