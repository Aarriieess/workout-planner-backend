package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.dto.UserProfileRequest;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.service.implementation.RuleBasedWorkoutServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rule-engine")
@RequiredArgsConstructor
public class  RuleBasedWorkoutController {

    private final RuleBasedWorkoutServiceImpl ruleBasedWorkoutService;

    @PostMapping("/generate")
        public ResponseEntity<WorkoutTemplateResponse> generatePlan(@Valid @RequestBody UserProfileRequest request) {
        WorkoutTemplateResponse generated = ruleBasedWorkoutService.generateTemplate(request);
        return ResponseEntity.ok(generated);
    }
}
