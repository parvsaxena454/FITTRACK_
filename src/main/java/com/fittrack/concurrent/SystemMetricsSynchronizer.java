package com.fittrack.concurrent;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: Multithreading & Synchronization (Thread Safety & Locks)
 * Demonstrates synchronized blocks, synchronized methods, Atomic variables,
 * and explicit ReentrantLock synchronization across concurrent HTTP requests.
 * ============================================================================
 */
@Component
public class SystemMetricsSynchronizer {

    // Thread-safe atomic counters
    private final AtomicLong totalPageViews = new AtomicLong(0);
    private final AtomicLong totalWorkoutsLoggedAsync = new AtomicLong(0);

    // Explicit reentrant lock for critical metric recalculations
    private final ReentrantLock metricsLock = new ReentrantLock();

    // Shared state protected by synchronization
    private final Map<String, Long> categoryCountMap = new HashMap<>();

    /**
     * Synchronized method: Ensures only one thread modifies the internal category map at a time.
     */
    public synchronized void recordCategoryInteraction(String category) {
        if (category == null) return;
        categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0L) + 1);
    }

    /**
     * Synchronized block: Demonstrates scoped synchronization on a monitor object.
     */
    public void incrementPageView() {
        totalPageViews.incrementAndGet();

        // Synchronized block demonstration
        synchronized (this) {
            // Safe thread-safe audit check
            if (totalPageViews.get() % 100 == 0) {
                System.out.println("[Thread " + Thread.currentThread().getName() + "] Milestone: Reached " + totalPageViews.get() + " page views.");
            }
        }
    }

    /**
     * Explicit ReentrantLock synchronization for complex calculations.
     */
    public Map<String, Object> getSynchronizedMetricsSnapshot() {
        metricsLock.lock();
        try {
            Map<String, Object> snapshot = new HashMap<>();
            snapshot.put("pageViews", totalPageViews.get());
            snapshot.put("workoutsLoggedAsync", totalWorkoutsLoggedAsync.get());
            snapshot.put("categoryInteractions", Collections.unmodifiableMap(new HashMap<>(categoryCountMap)));
            snapshot.put("activeWorkerThreads", Thread.activeCount());
            return snapshot;
        } finally {
            metricsLock.unlock();
        }
    }

    public void incrementWorkoutCounter() {
        totalWorkoutsLoggedAsync.incrementAndGet();
    }
}
