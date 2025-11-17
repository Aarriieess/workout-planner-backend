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
    date = "2025-11-17T19:10:16+0800",
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

    @Override
    public void updateEntityFromRequest(ExerciseRequest request, Exercise exercise) {
        if ( request == null ) {
            return;
        }

        exercise.setName( request.getName() );
        exercise.setPrimaryMuscleGroup( request.getPrimaryMuscleGroup() );
        if ( exercise.getSecondaryMuscleGroup() != null ) {
            List<MuscleGroup> list = request.getSecondaryMuscleGroup();
            if ( list != null ) {
                exercise.getSecondaryMuscleGroup().clear();
                exercise.getSecondaryMuscleGroup().addAll( list );
            }
            else {
                exercise.setSecondaryMuscleGroup( null );
            }
        }
        else {
            List<MuscleGroup> list = request.getSecondaryMuscleGroup();
            if ( list != null ) {
                exercise.setSecondaryMuscleGroup( new ArrayList<MuscleGroup>( list ) );
            }
        }
        exercise.setDescription( request.getDescription() );
        if ( exercise.getTargetGoals() != null ) {
            List<FitnessGoal> list1 = request.getTargetGoals();
            if ( list1 != null ) {
                exercise.getTargetGoals().clear();
                exercise.getTargetGoals().addAll( list1 );
            }
            else {
                exercise.setTargetGoals( null );
            }
        }
        else {
            List<FitnessGoal> list1 = request.getTargetGoals();
            if ( list1 != null ) {
                exercise.setTargetGoals( new ArrayList<FitnessGoal>( list1 ) );
            }
        }
        if ( exercise.getSuitableLevels() != null ) {
            List<FitnessLevel> list2 = request.getSuitableLevels();
            if ( list2 != null ) {
                exercise.getSuitableLevels().clear();
                exercise.getSuitableLevels().addAll( list2 );
            }
            else {
                exercise.setSuitableLevels( null );
            }
        }
        else {
            List<FitnessLevel> list2 = request.getSuitableLevels();
            if ( list2 != null ) {
                exercise.setSuitableLevels( new ArrayList<FitnessLevel>( list2 ) );
            }
        }
        if ( exercise.getWorkoutEnvironment() != null ) {
            List<WorkoutEnvironment> list3 = request.getWorkoutEnvironment();
            if ( list3 != null ) {
                exercise.getWorkoutEnvironment().clear();
                exercise.getWorkoutEnvironment().addAll( list3 );
            }
            else {
                exercise.setWorkoutEnvironment( null );
            }
        }
        else {
            List<WorkoutEnvironment> list3 = request.getWorkoutEnvironment();
            if ( list3 != null ) {
                exercise.setWorkoutEnvironment( new ArrayList<WorkoutEnvironment>( list3 ) );
            }
        }
        exercise.setExerciseType( request.getExerciseType() );
        exercise.setUnilateral( request.isUnilateral() );
    }
}
