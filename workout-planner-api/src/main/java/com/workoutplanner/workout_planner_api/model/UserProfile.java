package com.workoutplanner.workout_planner_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Enumerated(EnumType.STRING)
    private FitnessLevel fitnessLevel;

    @Enumerated(EnumType.STRING)
    private FitnessGoal fitnessGoal;

    @Enumerated(EnumType.STRING)
    private WorkoutEnvironment workoutEnvironment;

    private int trainingDays;


}
