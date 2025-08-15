package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.service.WorkoutTemplateService;
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

    @PostMapping
    public WorkoutTemplate createTemplate(@RequestBody WorkoutTemplate template) {
        return workoutTemplateService.createTemplate(template);
    }

    @PutMapping("/{id}")
    public WorkoutTemplate updateTemplate(@PathVariable Long id,
                                          @RequestBody WorkoutTemplate template) {
        return workoutTemplateService.updateTemplate(id, template);
    }

}
