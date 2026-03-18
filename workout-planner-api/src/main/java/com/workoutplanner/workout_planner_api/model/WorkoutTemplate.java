package com.workoutplanner.workout_planner_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class WorkoutTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private WorkoutSplit workoutSplit;

    @Enumerated(EnumType.STRING)
    private FitnessGoal fitnessGoal;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "workoutTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @OrderBy("dayIndex ASC, orderIndex ASC")
    @Builder.Default
    private List<PlanExercise> planExercises = new ArrayList<>();

    public void clearExercises() {
        if (this.planExercises != null) {
            for (PlanExercise exercise : planExercises) {
                exercise.setWorkoutTemplate(null);
            }
            planExercises.clear();
        }
    }

    public void addPlanExercise(PlanExercise planExercise) {
        planExercise.setWorkoutTemplate(this);
        this.planExercises.add(planExercise);
    }

    public void removeExercise(PlanExercise exercise) {
        this.planExercises.remove(exercise);
    }

    public void addDefaultPlanExercise(Exercise exercise) {

        int nextIndex = this.planExercises.size();

        PlanExercise planExercise = PlanExercise.builder()
                .exercise(exercise)
                .sets(3)
                .reps(10)
                .restSeconds(60)
                .orderIndex(nextIndex)
                .build();

        planExercise.setWorkoutTemplate(this);
        this.addPlanExercise(planExercise);
    }
}
