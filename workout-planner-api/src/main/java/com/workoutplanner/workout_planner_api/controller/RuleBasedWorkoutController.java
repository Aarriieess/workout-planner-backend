package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.auth.UserPrincipal;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateResponse;
import com.workoutplanner.workout_planner_api.service.implementation.RuleBasedWorkoutServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rule-engine")
@RequiredArgsConstructor
public class  RuleBasedWorkoutController {

    private final RuleBasedWorkoutServiceImpl ruleBasedWorkoutService;

    @PostMapping("/generate")
        public ResponseEntity<WorkoutTemplateResponse> generateTemplate(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        WorkoutTemplateResponse generated = ruleBasedWorkoutService.generateTemplate(userPrincipal);
        return ResponseEntity.ok(generated);
    }
}
