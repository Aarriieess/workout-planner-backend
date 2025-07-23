package com.workoutplanner.workout_planner_api.service.strategy;

import com.workoutplanner.workout_planner_api.model.WorkoutSplit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WorkoutStrategyFactory {

    private final Map<WorkoutSplit, WorkoutGenerationStrategy> strategies;

    public WorkoutStrategyFactory(List<WorkoutGenerationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        WorkoutGenerationStrategy::getSupportedSplit,
                        strategy -> strategy
                ));
    }

    public WorkoutGenerationStrategy getStrategy(WorkoutSplit split) {
        WorkoutGenerationStrategy strategy = strategies.get(split);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported workout split: " + split);
        }

        return strategy;
    }

    public List<WorkoutSplit> getAvailableSplits() {
        return strategies.keySet().stream().toList();
    }
}
