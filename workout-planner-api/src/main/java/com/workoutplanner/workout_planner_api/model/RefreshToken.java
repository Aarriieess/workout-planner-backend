package com.workoutplanner.workout_planner_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = false)
    private boolean expired = false;

    public boolean isActive() {
        return !revoked && !isExpired();
    }

    public boolean isExpired() {
        return expiryDate.isBefore(Instant.now()) || expired;
    }

    public void markRevoked() {
        this.revoked = true;
    }

    public void markExpired() {
        this.expired = true;
    }
}
