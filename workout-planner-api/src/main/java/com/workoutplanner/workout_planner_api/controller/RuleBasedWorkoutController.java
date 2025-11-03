package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.auth.UserPrincipal;
import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.service.implementation.RuleBasedWorkoutServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rule-engine")
@RequiredArgsConstructor
public class  RuleBasedWorkoutController {

    private final RuleBasedWorkoutServiceImpl ruleBasedWorkoutService;

    @PreAuthorize("#user.id == #request.userId")
    @PostMapping("/generate")
        public ResponseEntity<WorkoutTemplateResponse> generatePlan(
                UserPrincipal userPrincipal,
                @Valid @RequestBody UserProfileRequest request
        ) {
        WorkoutTemplateResponse generated = ruleBasedWorkoutService.generateTemplate(userPrincipal, request);
        return ResponseEntity.ok(generated);
    }
}
