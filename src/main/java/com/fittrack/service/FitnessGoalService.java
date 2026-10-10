package com.fittrack.service;

import com.fittrack.entity.FitnessGoal;
import com.fittrack.entity.User;
import com.fittrack.repository.FitnessGoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FitnessGoalService {

    private final FitnessGoalRepository fitnessGoalRepository;
    private final ActivityLogService activityLogService;

    public FitnessGoalService(FitnessGoalRepository fitnessGoalRepository, ActivityLogService activityLogService) {
        this.fitnessGoalRepository = fitnessGoalRepository;
        this.activityLogService = activityLogService;
    }

    @Transactional
    public FitnessGoal createGoal(User user, String title, String category, Double targetValue,
                                  Double currentValue, String unit, LocalDate startDate,
                                  LocalDate targetDate, String description) {
        FitnessGoal goal = new FitnessGoal(user, title, category, targetValue, currentValue, unit, startDate, targetDate, description);
        FitnessGoal saved = fitnessGoalRepository.save(goal);
        activityLogService.logActivity(user, "GOAL_CREATED",
                "Created fitness goal: '" + title + "' (Target: " + targetValue + " " + unit + ")", null);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<FitnessGoal> getUserGoals(User user) {
        return fitnessGoalRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public Optional<FitnessGoal> getGoalById(Long id) {
        return fitnessGoalRepository.findById(id);
    }

    @Transactional
    public FitnessGoal updateProgress(Long id, User user, Double newCurrentValue) {
        FitnessGoal goal = fitnessGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fitness goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized to modify this goal.");
        }

        goal.setCurrentValue(newCurrentValue);
        if (goal.getProgressPercentage() >= 100) {
            goal.setStatus("ACHIEVED");
            activityLogService.logActivity(user, "GOAL_ACHIEVED", "Achieved fitness goal: '" + goal.getTitle() + "'! 🎉", null);
        }

        return fitnessGoalRepository.save(goal);
    }

    @Transactional
    public FitnessGoal updateGoal(Long id, User user, String title, String category, Double targetValue,
                                  Double currentValue, String unit, LocalDate targetDate, String status, String description) {
        FitnessGoal goal = fitnessGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fitness goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized to modify this goal.");
        }

        goal.setTitle(title);
        goal.setCategory(category);
        goal.setTargetValue(targetValue);
        goal.setCurrentValue(currentValue);
        goal.setUnit(unit);
        goal.setTargetDate(targetDate);
        goal.setStatus(status);
        goal.setDescription(description);

        if (goal.getProgressPercentage() >= 100 && !"ACHIEVED".equals(status)) {
            goal.setStatus("ACHIEVED");
        }

        return fitnessGoalRepository.save(goal);
    }

    @Transactional
    public void deleteGoal(Long id, User user) {
        FitnessGoal goal = fitnessGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fitness goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized to delete this goal.");
        }

        fitnessGoalRepository.delete(goal);
    }

    @Transactional(readOnly = true)
    public long getActiveGoalsCount(User user) {
        return fitnessGoalRepository.countByUserAndStatus(user, "IN_PROGRESS");
    }

    @Transactional(readOnly = true)
    public long getAchievedGoalsCount(User user) {
        return fitnessGoalRepository.countByUserAndStatus(user, "ACHIEVED");
    }
}
