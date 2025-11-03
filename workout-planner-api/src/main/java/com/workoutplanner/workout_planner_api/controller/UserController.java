package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.dto.UserProfileResponse;
import com.workoutplanner.workout_planner_api.auth.UserPrincipal;
import com.workoutplanner.workout_planner_api.dto.UserResponse;
import com.workoutplanner.workout_planner_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(userService.getUserResponse(user.getId()));
    }

    @PreAuthorize("#user.id == userId")
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(userService.getUserProfile(user.getId()));
    }

    @PreAuthorize("#user.id == userId")
    @PutMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid UserProfileRequest request
            ) {
        return ResponseEntity.ok(userService.updateUserProfile(user.getId(), request));
    }
}
