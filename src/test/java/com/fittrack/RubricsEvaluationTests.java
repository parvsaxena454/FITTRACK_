package com.fittrack;

import com.fittrack.common.*;
import com.fittrack.concurrent.SystemMetricsSynchronizer;
import com.fittrack.entity.User;
import com.fittrack.entity.Workout;
import com.fittrack.exception.InvalidFitnessDataException;
import com.fittrack.exception.ResourceNotFoundException;
import com.fittrack.servlet.FitTrackSessionListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Academic Rubrics Compliance Test Suite")
public class RubricsEvaluationTests {

    @Nested
    @DisplayName("Rubric 1: OOP Implementation Tests")
    class OopRubricTests {

        @Test
        @DisplayName("Polymorphism: MetricProcessor generic implementations calculate correctly")
        void testPolymorphismInMetricProcessors() {
            MetricProcessor<Integer> calorieProcessor = new CalorieMetricProcessor();
            MetricProcessor<Double> distanceProcessor = new DistanceMetricProcessor();

            List<Integer> calorieData = Arrays.asList(300, 500, 400);
            List<Double> distanceData = Arrays.asList(3.5, 5.0, 4.5);

            double calTotal = calorieProcessor.calculateTotal(calorieData);
            double calAvg = calorieProcessor.calculateAverage(calorieData);
            assertEquals(1200.0, calTotal);
            assertEquals(400.0, calAvg);
            assertEquals("400 kcal", calorieProcessor.formatWithUnit(400));

            double distTotal = distanceProcessor.calculateTotal(distanceData);
            double distAvg = distanceProcessor.calculateAverage(distanceData);
            assertEquals(13.0, distTotal, 0.01);
            assertEquals(4.33, distAvg, 0.01);
            assertEquals("5.0 km", distanceProcessor.formatWithUnit(5.0));

            assertNotEquals(calorieProcessor.getMetricName(), distanceProcessor.getMetricName());
        }

        @Test
        @DisplayName("Exception Handling: Custom exceptions thrown with structured error codes")
        void testCustomExceptionHierarchy() {
            InvalidFitnessDataException dataEx = assertThrows(InvalidFitnessDataException.class, () -> {
                throw new InvalidFitnessDataException("Duration must be strictly positive");
            });
            assertEquals("INVALID_FITNESS_METRIC", dataEx.getErrorCode());

            ResourceNotFoundException notFoundEx = assertThrows(ResourceNotFoundException.class, () -> {
                throw new ResourceNotFoundException("User", 999L);
            });
            assertEquals("RESOURCE_NOT_FOUND", notFoundEx.getErrorCode());
            assertTrue(notFoundEx.getMessage().contains("999"));
        }

        @Test
        @DisplayName("Encapsulation: User and Workout entity integrity")
        void testEncapsulation() {
            User user = new User();
            user.setUsername("ansh");
            user.setEmail("ansh@fittrack.com");
            user.setWeight(75.0);
            user.setHeight(178.0);

            assertEquals("ansh", user.getUsername());
            assertEquals("ansh@fittrack.com", user.getEmail());
            assertEquals(75.0, user.getWeight());
            assertEquals(178.0, user.getHeight());
        }
    }

    @Nested
    @DisplayName("Rubric 2: Collections & Generics Tests")
    class CollectionsAndGenericsRubricTests {

        @Test
        @DisplayName("Generics: GenericApiResponse wraps diverse payloads")
        void testGenericApiResponse() {
            GenericApiResponse<String> stringResp = GenericApiResponse.success("Status OK", "SYSTEM_READY");
            assertTrue(stringResp.isSuccess());
            assertEquals("SYSTEM_READY", stringResp.getPayload());

            GenericApiResponse<Integer> intResp = GenericApiResponse.success("Count result", 42);
            assertEquals(42, intResp.getPayload());

            GenericApiResponse<Void> failResp = GenericApiResponse.failure("Access denied");
            assertFalse(failResp.isSuccess());
            assertNull(failResp.getPayload());
        }

        @Test
        @DisplayName("Collections & Streams: Grouping, filtering, and reduction")
        void testStreamApiAndCollections() {
            List<Workout> workouts = new ArrayList<>();
            workouts.add(createSampleWorkout("Running", 300, 30));
            workouts.add(createSampleWorkout("Running", 450, 45));
            workouts.add(createSampleWorkout("Swimming", 400, 40));
            workouts.add(createSampleWorkout("Cycling", 500, 60));

            // Stream Filter & Reduce
            int totalCalories = workouts.stream()
                    .mapToInt(Workout::getCaloriesBurned)
                    .sum();
            assertEquals(1650, totalCalories);

            // Stream GroupingBy Workout Type
            Map<String, List<Workout>> grouped = workouts.stream()
                    .collect(Collectors.groupingBy(Workout::getWorkoutType));
            assertEquals(3, grouped.keySet().size());
            assertEquals(2, grouped.get("Running").size());

            // TreeSet sorted collection
            TreeSet<Integer> sortedCalories = new TreeSet<>(
                    workouts.stream().map(Workout::getCaloriesBurned).collect(Collectors.toList())
            );
            assertEquals(300, sortedCalories.first());
            assertEquals(500, sortedCalories.last());

            // PriorityQueue prioritization
            PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
            pq.addAll(sortedCalories);
            assertEquals(500, pq.poll());
        }

        private Workout createSampleWorkout(String workoutType, int calories, int duration) {
            Workout w = new Workout();
            w.setTitle("Daily " + workoutType);
            w.setWorkoutType(workoutType);
            w.setCaloriesBurned(calories);
            w.setDurationMinutes(duration);
            w.setWorkoutDate(LocalDate.now());
            return w;
        }
    }

    @Nested
    @DisplayName("Rubric 3: Multithreading & Synchronization Tests")
    class MultithreadingRubricTests {

        @Test
        @DisplayName("Concurrency: SystemMetricsSynchronizer thread-safety with ReentrantLock and AtomicLong")
        void testConcurrentMetricSynchronization() throws InterruptedException {
            SystemMetricsSynchronizer synchronizer = new SystemMetricsSynchronizer();
            int threadCount = 10;
            int iterationsPerThread = 100;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        for (int j = 0; j < iterationsPerThread; j++) {
                            synchronizer.incrementPageView();
                            synchronizer.incrementWorkoutCounter();
                            synchronizer.recordCategoryInteraction("Running");
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            assertTrue(latch.await(5, TimeUnit.SECONDS));
            executor.shutdown();

            Map<String, Object> snapshot = synchronizer.getSynchronizedMetricsSnapshot();
            assertEquals((long) threadCount * iterationsPerThread, snapshot.get("pageViews"));
            assertEquals((long) threadCount * iterationsPerThread, snapshot.get("workoutsLoggedAsync"));

            @SuppressWarnings("unchecked")
            Map<String, Long> categoryMap = (Map<String, Long>) snapshot.get("categoryInteractions");
            assertEquals((long) threadCount * iterationsPerThread, categoryMap.get("Running"));
        }
    }

    @Nested
    @DisplayName("Rubric 5: Servlets & Listeners Tests")
    class ServletsAndWebRubricTests {

        @Test
        @DisplayName("Session Listener active session count tracking")
        void testFitTrackSessionListener() {
            int active = FitTrackSessionListener.getActiveSessionsCount();
            assertTrue(active >= 0);
        }
    }
}
