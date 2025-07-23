package com.workoutplanner.workout_planner_api.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class UserProfile {

    @Enumerated(EnumType.STRING)
    private FitnessLevel fitnessLevel;

    @Enumerated(EnumType.STRING)
    private FitnessGoal fitnessGoal;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<EquipmentType> equipmentType;

    @Enumerated(EnumType.STRING)
    private WorkoutSplit workoutSplit;

    private int trainingDays;

}
