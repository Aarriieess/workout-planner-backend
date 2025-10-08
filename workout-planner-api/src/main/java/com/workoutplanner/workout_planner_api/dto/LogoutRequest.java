package com.workoutplanner.workout_planner_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {
    private String refreshToken;
    private boolean allDevices = false;
}
