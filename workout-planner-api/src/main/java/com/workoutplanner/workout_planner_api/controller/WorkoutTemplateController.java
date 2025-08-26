package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.service.WorkoutTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/templates")
public class WorkoutTemplateController {

    private final WorkoutTemplateService workoutTemplateService;

    public WorkoutTemplateController(WorkoutTemplateService workoutTemplateService){
        this.workoutTemplateService = workoutTemplateService;
    }

    @GetMapping("/user/{userId}")
    public WorkoutTemplate getUserTemplate(@PathVariable Long userId) {
        return workoutTemplateService.getUserTemplate(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTemplate(
            @PathVariable Long id,
            @RequestBody WorkoutTemplateRequest request
    ) {
        workoutTemplateService.updateTemplate(id, request);
        return ResponseEntity.ok().build();
    }

}
