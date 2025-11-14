package com.workoutplanner.workout_planner_api.repo;
import com.workoutplanner.workout_planner_api.model.RefreshToken;
import com.workoutplanner.workout_planner_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByHashedToken(String hashedToken);

    List<RefreshToken> findAllByEmail(String email);

    void deleteByUserIdAndRevokedTrue(Long userId);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId")
    void revokeAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.token = :token AND rt.user.id = :userId")
    int revokeByTokenAndUserId(@Param("token") String token, @Param("userId") Long userId);
}
