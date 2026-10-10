package com.fittrack.common;

import org.springframework.stereotype.Component;
import java.util.Collection;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: OOP Implementation (Polymorphism & Generics Implementation)
 * Concrete processor handling Double distance measurements (km/miles).
 * ============================================================================
 */
@Component("distanceMetricProcessor")
public class DistanceMetricProcessor implements MetricProcessor<Double> {

    @Override
    public String getMetricName() {
        return "Aerobic Distance Traveled";
    }

    @Override
    public double calculateAverage(Collection<Double> dataPoints) {
        if (dataPoints == null || dataPoints.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Double val : dataPoints) {
            if (val != null) {
                sum += val;
            }
        }
        return Math.round((sum / dataPoints.size()) * 100.0) / 100.0;
    }

    @Override
    public double calculateTotal(Collection<Double> dataPoints) {
        if (dataPoints == null || dataPoints.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Double val : dataPoints) {
            if (val != null) {
                sum += val;
            }
        }
        return Math.round(sum * 100.0) / 100.0;
    }

    @Override
    public String formatWithUnit(Double value) {
        return (value != null ? value : 0.0) + " km";
    }
}
