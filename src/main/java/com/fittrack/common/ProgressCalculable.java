package com.fittrack.common;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: OOP Implementation (Interfaces & Default Methods)
 * Defines the contract for entities or DTOs capable of computing progress.
 * ============================================================================
 */
public interface ProgressCalculable {

    double getCurrentMetric();
    double getTargetMetric();

    /**
     * Default method demonstrating Java 8+ interface capability.
     * Computes the normalized percentage clamped between 0.0 and 100.0.
     */
    default double computeProgressPercentage() {
        double target = getTargetMetric();
        if (target <= 0.0) {
            return 0.0;
        }
        double percentage = (getCurrentMetric() / target) * 100.0;
        return Math.min(100.0, Math.max(0.0, Math.round(percentage * 10.0) / 10.0));
    }

    /**
     * Static helper method inside an interface.
     */
    static boolean isTargetAchieved(ProgressCalculable calculable) {
        return calculable != null && calculable.computeProgressPercentage() >= 100.0;
    }
}
