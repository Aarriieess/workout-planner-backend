package com.workoutplanner.workout_planner_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plan_exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"workoutTemplate"})
public class PlanExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(1)
    private int sets;

    @Min(1)
    private int reps;

    @Min(0)
    private int restSeconds;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column
    private  int dayIndex;

    @ManyToOne
    @JoinColumn(name = "workout_template_id")
    @JsonBackReference
    private WorkoutTemplate workoutTemplate;



}
