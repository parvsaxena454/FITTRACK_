package com.fittrack.repository;

import com.fittrack.entity.FitnessGoal;
import com.fittrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FitnessGoalRepository extends JpaRepository<FitnessGoal, Long> {

    List<FitnessGoal> findByUserOrderByCreatedAtDesc(User user);

    List<FitnessGoal> findByUserAndStatusOrderByCreatedAtDesc(User user, String status);

    long countByUserAndStatus(User user, String status);

    long countByUser(User user);
}
