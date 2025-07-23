package com.workoutplanner.workout_planner_api.service;

import com.workoutplanner.workout_planner_api.model.User;
import com.workoutplanner.workout_planner_api.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public User createUser(User user){
        return userRepo.save(user);
    }
}
