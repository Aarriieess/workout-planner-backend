package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.WorkoutSplit;

import java.util.List;

public class WorkoutTemplateRequest {
    Long id;
    String name;
    FitnessGoal fitnessGoal;
    WorkoutSplit workoutSplit;
    List<PlanExerciseRequest> planExerciseRequestList;
}
