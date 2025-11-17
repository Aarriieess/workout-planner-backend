package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.UserProfileResponse;
import com.workoutplanner.workout_planner_api.dto.UserResponse;
import com.workoutplanner.workout_planner_api.model.User;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-17T19:10:16+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserProfileResponse profile = null;
        Long id = null;
        String name = null;
        String email = null;

        profile = userProfileMapper.toResponse( user.getUserProfile() );
        id = user.getId();
        name = user.getName();
        email = user.getEmail();

        UserResponse userResponse = new UserResponse( id, name, email, profile );

        return userResponse;
    }
}
