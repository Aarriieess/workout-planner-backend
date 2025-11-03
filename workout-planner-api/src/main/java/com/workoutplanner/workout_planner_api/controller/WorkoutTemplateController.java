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
    public ResponseEntity<WorkoutTemplateResponse> getUserTemplate(@PathVariable Long userId) {
        return ResponseEntity.ok(workoutTemplateService.getUserTemplate(userId));
    }

    @PreAuthorize("@securityService.ownsTemplate(#user.id, #templateId")
    @PostMapping("/{templateId}/exercises")
    public ResponseEntity<WorkoutTemplateResponse> addExerciseToTemplate(
            @PathVariable Long templateId,
            @RequestBody @Valid PlanExerciseRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(workoutTemplateService.addExerciseToTemplate(
                templateId,
                request,
                user.getId()
        ));
    }

    @PreAuthorize("#user.id == #templade.user.id")
    @PutMapping("/{templateId}")
    public ResponseEntity<WorkoutTemplateResponse> updateTemplate(
            @PathVariable Long templateId,
            @RequestBody @Valid WorkoutTemplateRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(workoutTemplateService.updateTemplate(
                templateId,
                request,
                user.getId()
        ));
    }

    @PreAuthorize("@securityService.ownsTemplate(#user.id, #templateId")
    @DeleteMapping("/{templateId}/exercises/{planExerciseId}")
    public ResponseEntity<Void> removeExerciseFromTemplate(
            @PathVariable Long templateId,
            @PathVariable Long planExerciseId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        workoutTemplateService.removeExerciseFromTemplate(templateId, planExerciseId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
