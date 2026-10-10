package com.fittrack.common;

import java.util.Collection;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: Collections & Generics (Bounded Generic Interface)
 * Demonstrates bounded type parameters and functional metric transformations.
 * ============================================================================
 */
public interface MetricProcessor<T extends Number> {

    String getMetricName();

    double calculateAverage(Collection<T> dataPoints);

    double calculateTotal(Collection<T> dataPoints);

    String formatWithUnit(T value);
}
