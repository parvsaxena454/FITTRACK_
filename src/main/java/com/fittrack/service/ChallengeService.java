package com.fittrack.service;

import com.fittrack.entity.Challenge;
import com.fittrack.entity.ChallengeParticipant;
import com.fittrack.entity.User;
import com.fittrack.repository.ChallengeParticipantRepository;
import com.fittrack.repository.ChallengeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipantRepository participantRepository;
    private final ActivityLogService activityLogService;

    public ChallengeService(ChallengeRepository challengeRepository,
                            ChallengeParticipantRepository participantRepository,
                            ActivityLogService activityLogService) {
        this.challengeRepository = challengeRepository;
        this.participantRepository = participantRepository;
        this.activityLogService = activityLogService;
    }

    // Admin: Create Challenge
    @Transactional
    public Challenge createChallenge(String title, String description, String category, String difficulty,
                                    Double targetMetric, String targetUnit, LocalDate startDate,
                                    LocalDate endDate, String rewardBadge, User adminUser) {
        Challenge challenge = new Challenge(title, description, category, difficulty, targetMetric, targetUnit, startDate, endDate, rewardBadge);
        Challenge saved = challengeRepository.save(challenge);
        activityLogService.logActivity(adminUser, "CHALLENGE_CREATED", "Admin created new challenge: '" + title + "'", null);
        return saved;
    }

    // Admin: Update Challenge
    @Transactional
    public Challenge updateChallenge(Long id, String title, String description, String category,
                                    String difficulty, Double targetMetric, String targetUnit,
                                    LocalDate startDate, LocalDate endDate, String rewardBadge, boolean active) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

        challenge.setTitle(title);
        challenge.setDescription(description);
        challenge.setCategory(category);
        challenge.setDifficulty(difficulty);
        challenge.setTargetMetric(targetMetric);
        challenge.setTargetUnit(targetUnit);
        challenge.setStartDate(startDate);
        challenge.setEndDate(endDate);
        challenge.setRewardBadge(rewardBadge);
        challenge.setActive(active);

        return challengeRepository.save(challenge);
    }

    // Admin: Toggle active state
    @Transactional
    public boolean toggleChallengeActive(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));
        challenge.setActive(!challenge.isActive());
        challengeRepository.save(challenge);
        return challenge.isActive();
    }

    // Admin: Delete challenge
    @Transactional
    public void deleteChallenge(Long id) {
        challengeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAllByOrderByStartDateDesc();
    }

    @Transactional(readOnly = true)
    public List<Challenge> getActiveChallenges() {
        return challengeRepository.findByActiveTrueOrderByStartDateDesc();
    }

    @Transactional(readOnly = true)
    public Optional<Challenge> getChallengeById(Long id) {
        return challengeRepository.findById(id);
    }

    // User: Join Challenge
    @Transactional
    public ChallengeParticipant joinChallenge(User user, Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

        if (!challenge.isActive()) {
            throw new IllegalStateException("This challenge is currently inactive.");
        }

        if (participantRepository.existsByUserAndChallenge(user, challenge)) {
            throw new IllegalStateException("You are already participating in this challenge!");
        }

        ChallengeParticipant participant = new ChallengeParticipant(user, challenge);
        ChallengeParticipant saved = participantRepository.save(participant);
        activityLogService.logActivity(user, "CHALLENGE_JOINED", "Joined challenge: '" + challenge.getTitle() + "'", null);
        return saved;
    }

    // User: Update Progress
    @Transactional
    public ChallengeParticipant updateParticipantProgress(Long participantId, User user, Double progressDelta) {
        ChallengeParticipant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant record not found"));

        if (!participant.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized to update progress for another user.");
        }

        double newProgress = participant.getProgressValue() + progressDelta;
        participant.setProgressValue(newProgress);

        ChallengeParticipant saved = participantRepository.save(participant);
        activityLogService.logActivity(user, "CHALLENGE_PROGRESS",
                "Updated progress in '" + participant.getChallenge().getTitle() + "': "
                        + saved.getProgressValue() + "/" + participant.getChallenge().getTargetMetric()
                        + " " + participant.getChallenge().getTargetUnit() + " (" + saved.getProgressPercentage() + "%)", null);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<ChallengeParticipant> getUserParticipations(User user) {
        return participantRepository.findByUserOrderByJoinedDateDesc(user);
    }

    @Transactional(readOnly = true)
    public List<ChallengeParticipant> getChallengeLeaderboard(Challenge challenge) {
        return participantRepository.findByChallengeOrderByProgressPercentageDesc(challenge);
    }

    @Transactional(readOnly = true)
    public Optional<ChallengeParticipant> getUserParticipation(User user, Challenge challenge) {
        return participantRepository.findByUserAndChallenge(user, challenge);
    }

    @Transactional(readOnly = true)
    public long getTotalChallengesCount() {
        return challengeRepository.count();
    }

    @Transactional(readOnly = true)
    public long getTotalParticipantsCount() {
        return participantRepository.count();
    }
}
