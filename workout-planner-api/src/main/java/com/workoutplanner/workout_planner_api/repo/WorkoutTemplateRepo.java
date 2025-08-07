package com.workoutplanner.workout_planner_api.repo;

import com.workoutplanner.workout_planner_api.model.FitnessGoal;
import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutTemplateRepo extends JpaRepository<WorkoutTemplate, Long> {

    List<WorkoutTemplate> findByFitnessGoal(FitnessGoal fitnessGoal);
    List<WorkoutTemplate> findByFitnessGoalAndId(FitnessGoal fitnessGoal, Long id);

    Optional<WorkoutTemplate> findByUser(User user);


}
