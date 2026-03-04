package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.auth.UserPrincipal;
import com.workoutplanner.workout_planner_api.dto.*;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import com.workoutplanner.workout_planner_api.service.WorkoutTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<WorkoutTemplateResponse> createEmptyTemplate(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(workoutTemplateService.createEmptyTemplate(userPrincipal.getId()));
    }

    @PostMapping("/exercises")
    public ResponseEntity<PlanExerciseResponse> addExerciseToTemplate(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody AddExerciseRequest exerciseRequest) {

        PlanExerciseResponse response =
                workoutTemplateService.addExerciseToTemplateWithDefaults(user.getId(), exerciseRequest.getExerciseId());

        return ResponseEntity.ok(response);
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
