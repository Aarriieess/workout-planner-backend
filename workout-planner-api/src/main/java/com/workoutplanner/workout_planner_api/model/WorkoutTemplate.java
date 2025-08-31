package com.workoutplanner.workout_planner_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.workoutplanner.workout_planner_api.dto.PlanExerciseResponse;
import com.workoutplanner.workout_planner_api.dto.WorkoutTemplateRequest;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
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
    private List<PlanExercise> planExercises = new ArrayList<>();

    public void clearExercises() {
        if (this.planExercises != null) {
            for (PlanExercise exercise : planExercises) {
                exercise.setWorkoutTemplate(null);
            }
            planExercises.clear();
        }
    }

    public void addExercise(PlanExercise exercise) {
        exercise.setWorkoutTemplate(this);
        this.planExercises.add(exercise);
    }

    public void removeExercise(PlanExercise exercise) {
        this.planExercises.remove(exercise);
        exercise.setWorkoutTemplate(null);
    }

    public static WorkoutTemplate fromRequest(WorkoutTemplateRequest request) {
        WorkoutTemplate template = new WorkoutTemplate();
        template.updateFromRequest(request);
        return template;
    }

    public void updateFromRequest(WorkoutTemplateRequest request) {
        this.setName(request.getName());
        this.setWorkoutSplit(request.getWorkoutSplit());
        this.setFitnessGoal(request.getFitnessGoal());
    }
}
