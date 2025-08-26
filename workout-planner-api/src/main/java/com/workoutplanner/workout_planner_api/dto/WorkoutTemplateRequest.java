package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.WorkoutSplit;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutTemplateRequest {
    String name;
    FitnessGoal fitnessGoal;
    WorkoutSplit workoutSplit;
    List<PlanExerciseRequest> planExerciseRequestList;
}
