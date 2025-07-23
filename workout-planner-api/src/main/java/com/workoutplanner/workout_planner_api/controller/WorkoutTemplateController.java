package com.workoutplanner.workout_planner_api.controller;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import com.workoutplanner.workout_planner_api.service.WorkoutTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/templates")
public class WorkoutTemplateController {

    private final WorkoutTemplateService workoutTemplateService;

    public WorkoutTemplateController(WorkoutTemplateService workoutTemplateService){
        this.workoutTemplateService = workoutTemplateService;
    }

    @GetMapping
    public List<WorkoutTemplate> getTemplates(
            @RequestParam(required = false) FitnessGoal fitnessGoal,
            @RequestParam(required = false) Long id
    ){
        return workoutTemplateService.getTemplates(fitnessGoal, id);
    }

    @GetMapping("/{id}")
    public WorkoutTemplate getTemplateById(@PathVariable Long id){
        return workoutTemplateService.getTemplateById(id);
    }

    @GetMapping("/goals")
    public List<FitnessGoal> getAllGoals(){
        return Arrays.asList(FitnessGoal.values());
    }

    @PostMapping
    public WorkoutTemplate createTemplate(@RequestBody WorkoutTemplate template){
        return workoutTemplateService.createTemplate(template);
    }

    @PutMapping("/{id}")
    public WorkoutTemplate updateTemplate(@PathVariable Long id,
                                          @RequestBody WorkoutTemplate template){
        return workoutTemplateService.updateTemplate(id, template);
    }

    @DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable Long id){
        workoutTemplateService.deleteTemplate(id);
    }

}
