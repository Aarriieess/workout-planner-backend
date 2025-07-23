package com.workoutplanner.workout_planner_api.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workoutplanner.workout_planner_api.model.Exercise;
import com.workoutplanner.workout_planner_api.repo.ExerciseRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class ExerciseDataSeeder implements CommandLineRunner {

    private final ExerciseRepo exerciseRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();;

    public ExerciseDataSeeder(ExerciseRepo exerciseRepo){
        this.exerciseRepo = exerciseRepo;
    }

    @Override
    public void run(String... args) {
        if (exerciseRepo.count() == 0){
            try (InputStream inputStream = new ClassPathResource("data/exercise-seed-data.json").getInputStream()){
                List<Exercise> exerciseList = objectMapper.readValue(inputStream, new TypeReference<>() {});
                exerciseRepo.saveAll(exerciseList);
                System.out.println("Seeded " + exerciseList.size() + " exercise data");
            } catch (IOException e){
                System.err.println("Failed to load exercise seed :" + e.getMessage());
            }
        } else {
            System.out.println("Exercise data already exist. Skipping seed");
        }
    }
}
