package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.*;
import lombok.Data;

import java.util.List;
@Data
public class ExerciseRequest {

    private String name;
    private MuscleGroup primaryMuscleGroup;
    private List<MuscleGroup> secondaryMuscleGroup;
    private String description;
    private List<FitnessGoal> targetGoals;
    private List<FitnessLevel> suitableLevels;
    private List<WorkoutEnvironment> workoutEnvironment;
    private ExerciseType exerciseType;
    private boolean isUnilateral;
}
