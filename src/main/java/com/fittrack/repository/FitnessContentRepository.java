package com.fittrack.repository;

import com.fittrack.entity.FitnessContent;
import com.fittrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FitnessContentRepository extends JpaRepository<FitnessContent, Long> {

    List<FitnessContent> findByStatusOrderBySubmittedAtDesc(String status);

    List<FitnessContent> findByAuthorOrderBySubmittedAtDesc(User author);

    List<FitnessContent> findAllByOrderBySubmittedAtDesc();

    long countByStatus(String status);

    long countByAuthor(User author);
}
