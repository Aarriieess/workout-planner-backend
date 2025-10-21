package com.workoutplanner.workout_planner_api.mapper;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.dto.UserProfileResponse;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserProfileMapper {

    UserProfileResponse toResponse(UserProfile profile);
    UserProfile toEntity(UserProfileRequest request);

    void updateEntityFromRequest(
            UserProfileRequest request,
            @MappingTarget UserProfile profile
    );
}
