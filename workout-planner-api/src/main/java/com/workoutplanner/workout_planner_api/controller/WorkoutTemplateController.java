package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.auth.UserPrincipal;
import com.workoutplanner.workout_planner_api.dto.*;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.service.WorkoutTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/templates")
public class WorkoutTemplateController {

    private final WorkoutTemplateService workoutTemplateService;

    public WorkoutTemplateController(WorkoutTemplateService workoutTemplateService){
        this.workoutTemplateService = workoutTemplateService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WorkoutTemplateResponse> getUserTemplate(@PathVariable Long userId) {
        WorkoutTemplate template = workoutTemplateService.getUserTemplate(userId);
        return ResponseEntity.ok(WorkoutTemplateResponse.fromEntity(template));
    }

    @PostMapping("/{templateId}/exercises")
    public ResponseEntity<WorkoutTemplateResponse> addExerciseToTemplate(
            @PathVariable Long templateId,
            @RequestBody PlanExerciseRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        Long userId = user.getId();

        WorkoutTemplate template = workoutTemplateService.addExerciseToTemplate(
                templateId,
                request,
                userId);
        return ResponseEntity.ok(WorkoutTemplateResponse.fromEntity(template));
    }

    @PutMapping("/{templateId}/exercises/{planExerciseId}")
    public ResponseEntity<WorkoutTemplateResponse> updateTemplate(
            @PathVariable Long templateId,
            @RequestBody WorkoutTemplateRequest request,
            @AuthenticationPrincipal UserPrincipal user) {

        Long userId = user.getId();

        WorkoutTemplate updated = workoutTemplateService.updateTemplate(templateId, request, userId);
        return ResponseEntity.ok(WorkoutTemplateResponse.fromEntity(updated));
    }

    @DeleteMapping("/{templateId}/exercises{planExerciseId}")
    public ResponseEntity<Void> removeExerciseFromTemplate(
            @PathVariable Long templateId,
            @PathVariable Long planExerciseId,
            @AuthenticationPrincipal UserPrincipal user) {

        workoutTemplateService.removeExerciseFromTemplate(templateId, planExerciseId, user.getId());
        return ResponseEntity.noContent().build();
    }

}
