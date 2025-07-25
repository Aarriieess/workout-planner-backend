package com.workoutplanner.workout_planner_api.dto;

import com.workoutplanner.workout_planner_api.model.EquipmentType;
import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.FitnessLevel;
import com.workoutplanner.workout_planner_api.model.WorkoutSplit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@NotNull
public class UserProfileRequest {
    private Long userId; // required to fetch user
    private FitnessLevel fitnessLevel;
    private FitnessGoal fitnessGoal;

    @Size(min = 1)
    private List<EquipmentType> equipmentType;
    private WorkoutSplit workoutSplit;

    @Min(1)
    private int trainingDays;
}
