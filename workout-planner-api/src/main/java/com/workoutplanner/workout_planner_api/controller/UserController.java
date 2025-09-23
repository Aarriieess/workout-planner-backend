package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.dto.UserProfileResponse;
import com.workoutplanner.workout_planner_api.auth.UserPrincipal;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import com.workoutplanner.workout_planner_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal UserPrincipal user) {
        UserProfile userProfile = userService.getUser(user.getId()).getUserProfile();
        return ResponseEntity.ok(UserProfileResponse.fromEntity(userProfile));
    }

    @PutMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody UserProfileRequest request
            ) {

        UserProfile updated = userService.updateUserProfile(user.getId(), request);
        return ResponseEntity.ok(UserProfileResponse.fromEntity(updated));
    }
}
