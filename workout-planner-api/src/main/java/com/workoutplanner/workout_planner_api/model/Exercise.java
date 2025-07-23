package com.workoutplanner.workout_planner_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "planExercises"})
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MuscleGroup primaryMuscleGroup;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<MuscleGroup> secondaryMuscleGroup;

    private String description;

    private String difficulty;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<FitnessGoal> targetGoals;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<FitnessLevel> suitableLevels;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<EquipmentType> equipmentType;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @Enumerated(EnumType.STRING)
    private MovementPattern movementPattern;

    private boolean isUnilateral;

}
