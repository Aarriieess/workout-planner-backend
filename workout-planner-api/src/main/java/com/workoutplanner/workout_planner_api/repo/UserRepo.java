package com.workoutplanner.workout_planner_api.repo;

import com.workoutplanner.workout_planner_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
