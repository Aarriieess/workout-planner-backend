package com.workoutplanner.workout_planner_api.auth;

import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user;

        try {
            Long userId = Long.valueOf(identifier);
            user = userRepo.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not foundd: " + identifier));
        } catch (NumberFormatException e) {
            user = userRepo.findByEmail(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User not foundd: " + identifier));
        }

        return UserPrincipal.fromEntity(user);
    }
}
