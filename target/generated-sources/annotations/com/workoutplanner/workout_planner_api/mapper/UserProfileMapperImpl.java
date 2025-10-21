package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.dto.UserProfileResponse;
import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.FitnessLevel;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import com.workoutplanner.workout_planner_api.model.WorkoutEnvironment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-20T12:49:40+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class UserProfileMapperImpl implements UserProfileMapper {

    @Override
    public UserProfileResponse toResponse(UserProfile profile) {
        if ( profile == null ) {
            return null;
        }

        FitnessLevel fitnessLevel = null;
        FitnessGoal fitnessGoal = null;
        WorkoutEnvironment workoutEnvironment = null;
        int trainingDays = 0;

        fitnessLevel = profile.getFitnessLevel();
        fitnessGoal = profile.getFitnessGoal();
        workoutEnvironment = profile.getWorkoutEnvironment();
        trainingDays = profile.getTrainingDays();

        UserProfileResponse userProfileResponse = new UserProfileResponse( fitnessLevel, fitnessGoal, workoutEnvironment, trainingDays );

        return userProfileResponse;
    }

    @Override
    public UserProfile toEntity(UserProfileRequest request) {
        if ( request == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder userProfile = UserProfile.builder();

        userProfile.fitnessLevel( request.getFitnessLevel() );
        userProfile.fitnessGoal( request.getFitnessGoal() );
        userProfile.workoutEnvironment( request.getWorkoutEnvironment() );
        userProfile.trainingDays( request.getTrainingDays() );

        return userProfile.build();
    }
}
