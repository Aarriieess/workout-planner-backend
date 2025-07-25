package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.model.UserProfile;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.service.implementation.RuleBasedWorkoutServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rule-engine")
public class  RuleBasedWorkoutController {

    private final RuleBasedWorkoutServiceImpl ruleBasedWorkoutService;

    public RuleBasedWorkoutController(RuleBasedWorkoutServiceImpl ruleBasedWorkoutService) {
        this.ruleBasedWorkoutService = ruleBasedWorkoutService;
    }

    @PostMapping("/generate")
        public ResponseEntity<WorkoutTemplate> generatePlan(@Valid @RequestBody UserProfileRequest request) {
        WorkoutTemplate generated = ruleBasedWorkoutService.generateTemplate(request);
        return ResponseEntity.ok(generated);
    }
}
