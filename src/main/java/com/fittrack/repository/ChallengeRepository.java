package com.fittrack.repository;

import com.fittrack.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findByActiveTrueOrderByStartDateDesc();

    List<Challenge> findAllByOrderByStartDateDesc();
}
