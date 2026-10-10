package com.fittrack.repository;

import com.fittrack.entity.Challenge;
import com.fittrack.entity.ChallengeParticipant;
import com.fittrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {

    List<ChallengeParticipant> findByUserOrderByJoinedDateDesc(User user);

    List<ChallengeParticipant> findByChallengeOrderByProgressPercentageDesc(Challenge challenge);

    Optional<ChallengeParticipant> findByUserAndChallenge(User user, Challenge challenge);

    boolean existsByUserAndChallenge(User user, Challenge challenge);

    long countByChallenge(Challenge challenge);

    long countByUser(User user);

    long countByUserAndStatus(User user, String status);
}
