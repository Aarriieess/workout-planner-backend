package com.workoutplanner.workout_planner_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.workoutplanner.workout_planner_api.dto.ExerciseRequest;
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


    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<FitnessGoal> targetGoals;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<FitnessLevel> suitableLevels;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<WorkoutEnvironment> workoutEnvironment;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    private boolean isUnilateral;


    public static Exercise fromRequest(ExerciseRequest request) {
        Exercise exercise = new Exercise();
        exercise.updateFromRequest(request);
        return exercise;
    }

    public void updateFromRequest(ExerciseRequest request) {
        this.setName(request.getName());
        this.setPrimaryMuscleGroup(request.getPrimaryMuscleGroup());
        this.setSecondaryMuscleGroup(request.getSecondaryMuscleGroup());
        this.setDescription(request.getDescription());
        this.setExerciseType(request.getExerciseType());
        this.setTargetGoals(request.getTargetGoals());
        this.setSuitableLevels(request.getSuitableLevels());
        this.setWorkoutEnvironment(request.getWorkoutEnvironment());
        this.setUnilateral(request.isUnilateral());
    }
}
