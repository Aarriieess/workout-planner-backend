package com.workoutplanner.workout_planner_api.repo;

import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.model.WorkoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutTemplateRepo extends JpaRepository<WorkoutTemplate, Long> {
    Optional<WorkoutTemplate> findByUser(User user);
    Optional<WorkoutTemplate> findByUserId(Long userId);

    @Query("SELECT t FROM WorkoutTemplate t LEFT JOIN FETCH t.planExercises WHERE t.user.id = :userId")
    Optional<WorkoutTemplate> findByUserIdWithExercises(@Param("userId") Long userId);
}
