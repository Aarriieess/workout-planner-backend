package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.auth.UserPrincipal;
import com.workoutplanner.workout_planner_api.dto.*;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.service.WorkoutTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class WorkoutTemplateController {

    private final WorkoutTemplateService workoutTemplateService;


    @GetMapping("/me")
    public ResponseEntity<WorkoutTemplateResponse> getUserTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(workoutTemplateService.getUserTemplate(userPrincipal.getId()));
    }

    @PostMapping("exercises")
    public ResponseEntity<WorkoutTemplateResponse> addExerciseToTemplate(
            @RequestBody @Valid PlanExerciseRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(workoutTemplateService.addExerciseToTemplate(
                request,
                user.getId()
        ));
    }

    @PutMapping
    public ResponseEntity<WorkoutTemplateResponse> updateTemplate(
            @RequestBody @Valid WorkoutTemplateRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(workoutTemplateService.updateTemplate(
                request,
                user.getId()
        ));
    }

    @DeleteMapping("exercises/{planExerciseId}")
    public ResponseEntity<Void> removeExerciseFromTemplate(
            @PathVariable Long planExerciseId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        workoutTemplateService.removeExerciseFromTemplate(planExerciseId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
