package com.fittrack.repository;

import com.fittrack.entity.User;
import com.fittrack.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByUserOrderByWorkoutDateDescCreatedAtDesc(User user);

    List<Workout> findByUserAndWorkoutDateBetweenOrderByWorkoutDateAsc(User user, LocalDate startDate, LocalDate endDate);

    long countByUser(User user);

    @Query("SELECT COALESCE(SUM(w.caloriesBurned), 0) FROM Workout w WHERE w.user = :user")
    Integer getTotalCaloriesBurnedByUser(@Param("user") User user);

    @Query("SELECT COALESCE(SUM(w.durationMinutes), 0) FROM Workout w WHERE w.user = :user")
    Integer getTotalDurationMinutesByUser(@Param("user") User user);

    @Query("SELECT COUNT(w) FROM Workout w WHERE w.user = :user AND w.workoutDate >= :sinceDate")
    Long countWorkoutsSince(@Param("user") User user, @Param("sinceDate") LocalDate sinceDate);
}
