package com.fittrack.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "challenge_participants", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "challenge_id"})
})
public class ChallengeParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(nullable = false)
    private Double progressValue = 0.0;

    @Column(nullable = false)
    private Double progressPercentage = 0.0;

    @Column(nullable = false, length = 30)
    private String status = "IN_PROGRESS"; // IN_PROGRESS, COMPLETED, DROPPED

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedDate;

    private LocalDateTime completedDate;

    private LocalDateTime lastUpdated;

    public ChallengeParticipant() {
    }

    public ChallengeParticipant(User user, Challenge challenge) {
        this.user = user;
        this.challenge = challenge;
        this.progressValue = 0.0;
        this.progressPercentage = 0.0;
        this.status = "IN_PROGRESS";
    }

    @PrePersist
    protected void onCreate() {
        this.joinedDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public void recalculateProgress() {
        if (challenge != null && challenge.getTargetMetric() != null && challenge.getTargetMetric() > 0) {
            double pct = (this.progressValue / challenge.getTargetMetric()) * 100.0;
            this.progressPercentage = Math.min(100.0, Math.max(0.0, Math.round(pct * 10.0) / 10.0));
            if (this.progressPercentage >= 100.0) {
                this.status = "COMPLETED";
                if (this.completedDate == null) {
                    this.completedDate = LocalDateTime.now();
                }
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Double getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(Double progressValue) {
        this.progressValue = progressValue;
        recalculateProgress();
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDateTime joinedDate) {
        this.joinedDate = joinedDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
