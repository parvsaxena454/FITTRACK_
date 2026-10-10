package com.fittrack.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "challenges")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 50)
    private String category; // Cardio, Strength, Endurance, Consistency, Flexibility

    @Column(nullable = false, length = 30)
    private String difficulty; // Beginner, Intermediate, Advanced, Elite

    @Column(nullable = false)
    private Double targetMetric; // e.g. 30 (days), 100 (km), 5000 (calories)

    @Column(nullable = false, length = 30)
    private String targetUnit; // days, km, workouts, kcal, steps

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(length = 100)
    private String rewardBadge; // e.g. "Core Titan", "Centurion Rider", "Iron Will"

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeParticipant> participants = new ArrayList<>();

    public Challenge() {
    }

    public Challenge(String title, String description, String category, String difficulty, Double targetMetric, String targetUnit, LocalDate startDate, LocalDate endDate, String rewardBadge) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.targetMetric = targetMetric;
        this.targetUnit = targetUnit;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rewardBadge = rewardBadge;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Double getTargetMetric() {
        return targetMetric;
    }

    public void setTargetMetric(Double targetMetric) {
        this.targetMetric = targetMetric;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getRewardBadge() {
        return rewardBadge;
    }

    public void setRewardBadge(String rewardBadge) {
        this.rewardBadge = rewardBadge;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ChallengeParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ChallengeParticipant> participants) {
        this.participants = participants;
    }
}
