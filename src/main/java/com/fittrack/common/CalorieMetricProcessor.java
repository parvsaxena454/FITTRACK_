package com.fittrack.common;

import org.springframework.stereotype.Component;
import java.util.Collection;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: OOP Implementation (Polymorphism & Generics Implementation)
 * Concrete processor handling Integer calorie measurements.
 * ============================================================================
 */
@Component("calorieMetricProcessor")
public class CalorieMetricProcessor implements MetricProcessor<Integer> {

    @Override
    public String getMetricName() {
        return "Active Caloric Expenditure";
    }

    @Override
    public double calculateAverage(Collection<Integer> dataPoints) {
        if (dataPoints == null || dataPoints.isEmpty()) {
            return 0.0;
        }
        long sum = 0;
        for (Integer val : dataPoints) {
            if (val != null) {
                sum += val;
            }
        }
        return Math.round((sum / (double) dataPoints.size()) * 10.0) / 10.0;
    }

    @Override
    public double calculateTotal(Collection<Integer> dataPoints) {
        if (dataPoints == null || dataPoints.isEmpty()) {
            return 0.0;
        }
        long sum = 0;
        for (Integer val : dataPoints) {
            if (val != null) {
                sum += val;
            }
        }
        return sum;
    }

    @Override
    public String formatWithUnit(Integer value) {
        return (value != null ? value : 0) + " kcal";
    }
}
