package com.fittrack.service;

import com.fittrack.entity.User;
import com.fittrack.entity.Workout;
import com.fittrack.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final ActivityLogService activityLogService;

    public WorkoutService(WorkoutRepository workoutRepository, ActivityLogService activityLogService) {
        this.workoutRepository = workoutRepository;
        this.activityLogService = activityLogService;
    }

    @Transactional
    public Workout logWorkout(User user, String title, String workoutType, Integer durationMinutes,
                             Integer caloriesBurned, LocalDate workoutDate, String intensity, String notes) {
        Workout workout = new Workout(user, title, workoutType, durationMinutes, caloriesBurned, workoutDate, intensity, notes);
        Workout saved = workoutRepository.save(workout);
        activityLogService.logActivity(user, "WORKOUT_LOGGED",
                "Logged " + workoutType + " session: '" + title + "' (" + caloriesBurned + " kcal, " + durationMinutes + " min)", null);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Workout> getUserWorkouts(User user) {
        return workoutRepository.findByUserOrderByWorkoutDateDescCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public Optional<Workout> getWorkoutById(Long id) {
        return workoutRepository.findById(id);
    }

    @Transactional
    public Workout updateWorkout(Long id, User user, String title, String workoutType, Integer durationMinutes,
                                 Integer caloriesBurned, LocalDate workoutDate, String intensity, String notes) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found"));

        if (!workout.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized to modify this workout.");
        }

        workout.setTitle(title);
        workout.setWorkoutType(workoutType);
        workout.setDurationMinutes(durationMinutes);
        workout.setCaloriesBurned(caloriesBurned);
        workout.setWorkoutDate(workoutDate);
        workout.setIntensity(intensity);
        workout.setNotes(notes);

        return workoutRepository.save(workout);
    }

    @Transactional
    public void deleteWorkout(Long id, User user) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found"));

        if (!workout.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized to delete this workout.");
        }

        workoutRepository.delete(workout);
        activityLogService.logActivity(user, "WORKOUT_DELETED", "Deleted workout: " + workout.getTitle(), null);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getUserWorkoutStats(User user) {
        Map<String, Object> stats = new HashMap<>();
        long totalCount = workoutRepository.countByUser(user);
        int totalCalories = workoutRepository.getTotalCaloriesBurnedByUser(user);
        int totalMinutes = workoutRepository.getTotalDurationMinutesByUser(user);
        long past7DaysCount = workoutRepository.countWorkoutsSince(user, LocalDate.now().minusDays(7));

        stats.put("totalWorkouts", totalCount);
        stats.put("totalCalories", totalCalories);
        stats.put("totalMinutes", totalMinutes);
        stats.put("past7DaysCount", past7DaysCount);
        stats.put("totalHours", Math.round((totalMinutes / 60.0) * 10.0) / 10.0);
        return stats;
    }

    @Transactional(readOnly = true)
    public long getTotalWorkoutsCount() {
        return workoutRepository.count();
    }
}
