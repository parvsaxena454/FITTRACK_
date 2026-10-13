# 📘 FITTRACK - ACADEMIC PROJECT SUBMISSION & VIVA DEFENSE GUIDE

**Project Title:** FitTrack: Online Fitness Tracking System  
**Group Leader:** Ansh Kataria (25SCSE1180706)  
**Group Members:** Parv Saxena, Anand Soni, Aniket Raj  
**Evaluation:** Java Enterprise & Object-Oriented Software Engineering (CSE / AI & ML)

---

## 🎯 1. Executive Summary
FitTrack is an enterprise-grade full-stack Java web application designed to monitor, analyze, and manage athletic and personal health routines. It implements strict role-based access control (Admin & End-User), dynamic progress leaderboards, asynchronous workout processing, pure JDBC high-speed data access, and direct Servlet-level monitoring.

---

## 🎓 2. Rubrics Defense & Technical Walkthrough (For Viva Voce)

### 🧩 Component 1: Object-Oriented Programming (OOP) Implementation
*Where in the code:*
1. **Inheritance & Abstraction:**
   - Interface `com.fittrack.common.MetricProcessor<T>` defines abstract contracts for processing fitness data.
   - Interface `com.fittrack.common.ProgressCalculable` outlines methods required for tracking milestone progress.
2. **Polymorphism:**
   - `CalorieMetricProcessor` and `DistanceMetricProcessor` both implement `MetricProcessor<Workout>`, providing polymorphic implementations of calorie burn vs distance pacing.
3. **Encapsulation:**
   - All entity models (`User`, `Workout`, `FitnessGoal`, `Challenge`, etc.) enforce strict encapsulation with private attributes, validation annotations, and public getters/setters.
4. **Custom Exception Hierarchy:**
   - Base exception: `com.fittrack.exception.FitTrackException` (extends `RuntimeException`).
   - Specialized exceptions: `ResourceNotFoundException` and `InvalidFitnessDataException`.
   - Centralized handler: `com.fittrack.exception.GlobalExceptionHandler` annotated with `@ControllerAdvice` to gracefully capture and format exceptions.

---

### 📦 Component 2: Collections Framework & Generics
*Where in the code:*
1. **Generics:**
   - `com.fittrack.common.GenericApiResponse<T>`: A generic response wrapper standardizing API payloads with timestamp, status, error messages, and payload data `<T>`.
   - `com.fittrack.common.MetricProcessor<T>`: Generic type-parameterized processing interface.
2. **Java Collections:**
   - `java.util.List` & `ArrayList`: Used for ordered collection of workouts, goals, and challenges.
   - `java.util.Map` & `HashMap`: Grouping analytics data by workout category and month.
   - `java.util.concurrent.ConcurrentHashMap`: Thread-safe caching and memory-stored metrics in `SystemMetricsSynchronizer`.
   - `java.util.TreeSet`: Sorting leaderboards and challenge participants based on completion percentages.
   - `java.util.PriorityQueue`: Prioritizing urgent goals and upcoming challenge milestones.
3. **Java 8+ Stream API:**
   - Used extensively across `WorkoutService`, `FitnessGoalService`, and `DashboardController` for:
     - Filtering workouts by category and date ranges.
     - Mapping entities to metric values (`mapToInt(Workout::getCaloriesBurned)`).
     - Reducing / summing totals (`summaryStatistics()`, `sum()`, `average()`).
     - Collecting to Maps and sorted lists (`Collectors.groupingBy()`, `Collectors.toList()`).

---

### ⚡ Component 3: Multithreading & Concurrency
*Where in the code:*
1. **ThreadPool Configuration:**
   - `com.fittrack.concurrent.AsyncConfig`: Configures a custom `ThreadPoolTaskExecutor` bean with:
     - Core Pool Size: 5
     - Max Pool Size: 10
     - Queue Capacity: 50
     - Thread Name Prefix: `FitTrack-Async-`
2. **Asynchronous Non-Blocking Processing:**
   - `com.fittrack.concurrent.AsyncAnalyticsService`: Uses Spring's `@Async("taskExecutor")` and returns `CompletableFuture<Map<String, Object>>` to compute heavy workout statistics in background threads without blocking user requests.
3. **Thread Synchronization & Locks:**
   - `com.fittrack.concurrent.SystemMetricsSynchronizer`:
     - Synchronized methods (`public synchronized void recordWorkoutEvent(...)`) ensuring mutual exclusion.
     - `java.util.concurrent.locks.ReentrantLock`: Explicit locking for thread-safe access to shared challenge state counters.
     - `java.util.concurrent.atomic.AtomicLong`: Lock-free atomic increment counters for total workouts and active sessions.

---

### 💾 Component 4: Pure JDBC Database Operations
*Where in the code:*
1. **Direct Connection Management:**
   - `com.fittrack.jdbc.JdbcConnectionFactory`: Manages raw JDBC connections from the underlying DataSource.
2. **Direct DAO Implementation:**
   - `com.fittrack.jdbc.FitTrackJdbcOperations` (interface) and `com.fittrack.jdbc.FitTrackJdbcDaoImpl` (implementation).
3. **Pure JDBC Concepts Demonstrated:**
   - `java.sql.PreparedStatement`: Parameterized SQL statements preventing SQL injection attacks.
   - `java.sql.ResultSet`: Raw iteration and mapping (`rs.next()`, `rs.getString()`, `rs.getInt()`).
   - **Transaction Management:** Explicit transaction control with `connection.setAutoCommit(false)`, `connection.commit()`, and `connection.rollback()` in `catch` blocks.
   - **Batch Execution:** High-speed batch insertion using `statement.addBatch()` and `statement.executeBatch()`.

---

### 🌐 Component 5: Servlets & Web Infrastructure
*Where in the code:*
1. **Pure HttpServlet:**
   - `com.fittrack.servlet.DirectMetricsServlet`: Extends `jakarta.servlet.http.HttpServlet`. Implements `doGet(HttpServletRequest, HttpServletResponse)` to output live runtime JSON telemetry directly at `/api/servlet/metrics`.
2. **Jakarta Filter:**
   - `com.fittrack.servlet.AuditLoggingFilter`: Implements `jakarta.servlet.Filter`. Intercepts incoming HTTP requests, records request start time, logs URI, and computes response execution latency.
3. **Session Listener:**
   - `com.fittrack.servlet.FitTrackSessionListener`: Implements `jakarta.servlet.http.HttpSessionListener`. Listens for `sessionCreated` and `sessionDestroyed` lifecycle events to maintain an accurate real-time active user count.
4. **Registration:**
   - `com.fittrack.servlet.ServletConfig`: Registers the Servlet, Filter, and Listener using Spring Boot's `ServletRegistrationBean`, `FilterRegistrationBean`, and `ServletListenerRegistrationBean`.

---

## 🗄️ 3. Database Schema Overview (Slide 7)
The project comes with `schema-mysql.sql` containing standard DDL for all 8 entities:
1. `users` (User demographics, roles, password hashes)
2. `workouts` (Workout logs, categories, calories, duration)
3. `fitness_goals` (User goals, target values, completion flags)
4. `challenges` (Community challenges, badges, duration)
5. `challenge_participants` (User challenge enrollments and progress)
6. `fitness_content` (Articles, guides, review statuses)
7. `system_settings` (Application configuration key-values)
8. `activity_logs` (Audit logging and tracking)

---

## 🧪 4. Quick Testing & Demo Checklist
1. **Start Application:** Double-click `run.bat` or run `mvn spring-boot:run`.
2. **Login as Leader (Ansh Kataria):** Click "Ansh Kataria (Leader)" button on login page, click "Sign In".
3. **Log a Workout:** Go to "Workouts" -> "Log New Workout" -> fill in details -> Save. Observe immediate recalculation of total calories burned and statistics.
4. **Inspect Pure Servlet:** Navigate to `http://localhost:8080/api/servlet/metrics` to show the examiner the raw Servlet response outputting live JVM memory, active sessions, and request counts.
5. **Login as Admin:** Log out, click "Fill Admin" button, and log in. Navigate to Admin Dashboard to review user accounts, approve pending fitness guides, and view system activity logs.

---

## 🚀 5. Uploading to GitHub
1. Create a new empty repository on your GitHub account.
2. In the project root, double-click **`push_to_github.bat`**.
3. Paste your repository URL and press **Enter**.
4. The script will configure the remote and push all source files directly to the `main` branch.
