package com.fittrack.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_goals")
public class FitnessGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 50)
    private String category; // Weight Loss, Muscle Gain, Weekly Workouts, Running Distance, Daily Steps, Calorie Burn

    @Column(nullable = false)
    private Double targetValue;

    @Column(nullable = false)
    private Double currentValue;

    @Column(nullable = false, length = 30)
    private String unit; // kg, km, steps, workouts, kcal

    private LocalDate startDate;

    private LocalDate targetDate;

    @Column(nullable = false, length = 30)
    private String status = "IN_PROGRESS"; // IN_PROGRESS, ACHIEVED, PAUSED

    @Column(length = 500)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public FitnessGoal() {
    }

    public FitnessGoal(User user, String title, String category, Double targetValue, Double currentValue, String unit, LocalDate startDate, LocalDate targetDate, String description) {
        this.user = user;
        this.title = title;
        this.category = category;
        this.targetValue = targetValue;
        this.currentValue = currentValue;
        this.unit = unit;
        this.startDate = startDate;
        this.targetDate = targetDate;
        this.description = description;
        this.status = "IN_PROGRESS";
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.startDate == null) {
            this.startDate = LocalDate.now();
        }
    }

    // Progress percentage helper
    public int getProgressPercentage() {
        if (targetValue == null || targetValue <= 0 || currentValue == null) {
            return 0;
        }
        if ("Weight Loss".equalsIgnoreCase(category)) {
            // For weight loss, if current <= target, it's 100%
            if (currentValue <= targetValue) return 100;
            // Otherwise calculate relative progression if start is higher
            return Math.min(100, Math.max(0, (int) Math.round((targetValue / currentValue) * 100.0)));
        } else {
            int pct = (int) Math.round((currentValue / targetValue) * 100.0);
            return Math.min(100, Math.max(0, pct));
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
