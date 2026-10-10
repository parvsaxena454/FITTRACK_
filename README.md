# 🏋️ FITTRACK: Online Fitness Tracking System

> **Enterprise-Grade Full-Stack Java Web Application for Fitness, Workout & Health Analytics**  
> Developed strictly adhering to Computer Science Academic Project Rubrics & Evaluation Standards.

---

## 👥 Project Team & Academic Credentials

| Role | Name | University Registration No. | Specialization / Branch |
|---|---|---|---|
| **Group Leader** | **Ansh** | `25SCSE1180706` | B.Tech Computer Science & Engineering (AI & ML) |
| **Group Member** | **Aryan Bhardwaj** | `25SCSE1180192` | B.Tech Computer Science & Engineering |
| **Group Member** | **Aryaman Singh** | `25SCSE1180867` | B.Tech Computer Science & Engineering |

---

## 🚀 1-Click Quick Start (Windows)

### Option 1: 1-Click Launch Script
Double-click **`run.bat`** in the project root directory, or execute in PowerShell / CMD:
```powershell
.\run.bat
```
The application will automatically compile and launch on port **8080**.

### Option 2: Run via Maven
```powershell
mvn spring-boot:run
```

Once started, open your web browser and navigate to:
```
http://localhost:8080/
```

---

## 🔑 Pre-Configured Demo Accounts (1-Click Fill on Login Screen)

| Role | Username / Email | Password | Access Rights & Modules |
|---|---|---|---|
| **System Administrator** | `admin` / `admin@fittrack.com` | `admin123` | Full Admin Dashboard, User Account Ban/Promote, Challenge Creation, Editorial Review Queue, Audit Logs, System Settings |
| **End-User (Team Leader)** | `ansh` / `ansh@fittrack.com` | `password123` | Full Workout Logging, Multi-Metric Goals, Community Challenges, Article Submissions, BMI & Weight Analytics |
| **End-User (Member)** | `aryan` / `aryan@fittrack.com` | `password123` | Calisthenics Routine Tracking, Challenge Progress, Leaderboard Participation |
| **End-User (Member)** | `aryaman` / `aryaman@fittrack.com` | `password123` | Strength & Powerlifting Logs, Goal Milestones, Fitness Article Submissions |

*(Note: The login page features quick 1-click test credential fill buttons for immediate viva demonstration!)*

---

## 📊 Academic Rubrics Compliance Matrix

This project was built from the ground up to rigorously demonstrate all required Java Enterprise & Core concepts:

| Rubric Component | Conceptual Requirement | Exact Implementation in FitTrack Codebase |
|---|---|---|
| **1. OOP Implementation** | Abstraction, Inheritance, Polymorphism, Encapsulation, Custom Exception Hierarchy | • Interfaces: `MetricProcessor<T>`, `ProgressCalculable`<br>• Polymorphism: `CalorieMetricProcessor`, `DistanceMetricProcessor`<br>• Custom Exceptions: `FitTrackException`, `InvalidFitnessDataException`, `ResourceNotFoundException`<br>• Global Centralized Handling: `GlobalExceptionHandler` with `@ControllerAdvice` |
| **2. Collections & Generics** | Generic Classes/Interfaces, Collections Framework, Modern Stream API | • Generic Classes: `GenericApiResponse<T>`, `MetricProcessor<T>`<br>• Collections: `List`, `Map`, `TreeSet` (sorted leaderboards), `PriorityQueue` (challenge urgency), `ConcurrentHashMap` (in-memory thread-safe metrics)<br>• Streams: Stream filtering, grouping, mapping, reduction, and collector summaries across workout analytics |
| **3. Multithreading & Synchronization** | `@Async`, `CompletableFuture`, Custom Thread Pool, Synchronized Blocks, Locks & Atomic Primitives | • Thread Pool: `AsyncConfig` configuring dedicated `ThreadPoolTaskExecutor`<br>• Asynchronous Tasks: `AsyncAnalyticsService` running parallel non-blocking computations<br>• Thread-Safety: `SystemMetricsSynchronizer` featuring `synchronized` blocks, `ReentrantLock`, and `AtomicLong` counters |
| **4. Database Operations & Pure JDBC** | Pure JDBC Driver, `PreparedStatement`, `ResultSet`, Transactions, Connection Pooling | • Pure JDBC Interface: `FitTrackJdbcOperations`<br>• Pure DAO Implementation: `FitTrackJdbcDaoImpl` bypassing ORM for high-throughput batching, raw `ResultSet` extraction, and explicit transaction boundaries (`setAutoCommit(false)`, `commit()`, `rollback()`)<br>• JDBC Connection Factory: `JdbcConnectionFactory` with Hikari connection pooling |
| **5. Servlets & Web Integration** | Pure `HttpServlet`, `jakarta.servlet.Filter`, `HttpSessionListener`, Web Lifecycle | • Direct Servlet: `DirectMetricsServlet` (`/metrics/live`) rendering real-time JSON runtime analytics<br>• Servlet Filter: `AuditLoggingFilter` intercepting all incoming HTTP requests to compute response latency & log URI access<br>• Session Listener: `FitTrackSessionListener` tracking real-time active user HTTP sessions<br>• Registration: `ServletConfig` utilizing Spring Boot Servlet registration beans |
| **6. GitHub & Code Readiness** | Version control cleanliness, documentation, database script, packaging | • Comprehensive `.gitignore`<br>• Standalone relational DDL: `schema-mysql.sql`<br>• 1-Click push script: `push_to_github.bat`<br>• 1-Click runner: `run.bat` |

---

## 🛠️ Complete Technology Stack

- **Backend Platform**: Java 17 / 21 LTS, Spring Boot 3.3.4
- **Web & Security**: Spring Security 6 (BCrypt password encoding, CSRF protection, Role-Based Access Control), Jakarta Servlet API 6.0
- **Data Persistence**:
  - Pure JDBC API (`java.sql.Connection`, `java.sql.PreparedStatement`, `java.sql.ResultSet`)
  - Spring Data JPA & Hibernate ORM
  - In-Memory H2 Engine (with MySQL dialect mode) for instant zero-dependency execution
  - Production-ready MySQL connector & external configuration profile (`application-mysql.properties`)
- **Frontend Architecture**: Thymeleaf 3 Template Engine, HTML5, Modern CSS3 Neon Dark Theme (`#0b0f19` background, `#ccff00` accents), Bootstrap 5 Grid, Chart.js Visualizations
- **Build & Dependency Management**: Apache Maven 3.9.9

---

## 🗄️ Relational Database Schema (Slide 7 Architecture)

The system manages 8 relational entities with complete foreign key integrity, defined in `schema-mysql.sql`:

1. **`users`**: User identity, hashed credentials, demographic attributes (age, weight, height, fitness level, bio), role (`ROLE_USER` or `ROLE_ADMIN`), active status.
2. **`workouts`**: Workout logs with category, title, duration in minutes, calories burned, date, intensity level, and personal notes.
3. **`fitness_goals`**: Measurable targets (goal type, target value, current value, unit, target date, completion status).
4. **`challenges`**: Community fitness events (target metric, start/end dates, badge reward, active status).
5. **`challenge_participants`**: Many-to-many relationship tracking user enrollment, progress, completion status, and enrollment timestamp.
6. **`fitness_content`**: Articles, guides, and nutrition plans submitted by users with editorial review queue (`PENDING`, `APPROVED`, `REJECTED`) and reviewer feedback.
7. **`system_settings`**: Key-value administrative parameters (system name, banner announcements, registration toggles, maintenance status).
8. **`activity_logs`**: System audit trail capturing user actions, IP addresses, entity types, and timestamps.

```
 [users] 1 ────────────< * [workouts]
    │    1 ────────────< * [fitness_goals]
    │    1 ────────────< * [challenge_participants] >──────────── 1 [challenges]
    │    1 ────────────< * [fitness_content]
    └──── 1 ────────────< * [activity_logs]
```

---

## 📂 Source Code Structure & Organization

```
fittrack/
├── .gitignore                          # Clean Git exclusion rules
├── pom.xml                             # Maven project dependencies and build configuration
├── README.md                           # Master Project Documentation & Rubrics Matrix
├── PROJECT_SUBMISSION_GUIDE.md         # Academic Viva, Code Walkthrough & Defense Guide
├── run.bat                             # 1-Click Windows execution script
├── push_to_github.bat                  # 1-Click GitHub repository push automation
├── schema-mysql.sql                    # Standalone MySQL DDL table creation script
└── src/
    └── main/
        ├── java/com/fittrack/
        │   ├── FitTrackApplication.java            # Main Spring Boot Application Entrypoint
        │   ├── common/                             # [Rubric 1 & 2] Generics & OOP Interfaces
        │   │   ├── GenericApiResponse.java         # Generic Response Wrapper <T>
        │   │   ├── MetricProcessor.java            # Generic Interface <T>
        │   │   ├── CalorieMetricProcessor.java     # Polymorphic Implementation
        │   │   ├── DistanceMetricProcessor.java    # Polymorphic Implementation
        │   │   └── ProgressCalculable.java         # Calculation Contract Interface
        │   ├── concurrent/                         # [Rubric 3] Multithreading & Concurrency
        │   │   ├── AsyncConfig.java                # Custom ThreadPoolTaskExecutor
        │   │   ├── AsyncAnalyticsService.java      # CompletableFuture & @Async Computations
        │   │   └── SystemMetricsSynchronizer.java  # synchronized, ReentrantLock, AtomicLong
        │   ├── config/
        │   │   └── DataInitializer.java            # Seeds Default Users, Challenges, & Logs
        │   ├── controller/                         # Web Presentation Layer (Spring MVC)
        │   │   ├── AuthController.java             # Login & Registration Flow
        │   │   ├── DashboardController.java        # User Analytics & Dashboard
        │   │   ├── WorkoutController.java          # Workout CRUD Endpoints
        │   │   ├── FitnessGoalController.java      # Goal Tracking Endpoints
        │   │   ├── ChallengeController.java        # Community Challenges
        │   │   ├── FitnessContentController.java   # Content & Guide Submissions
        │   │   ├── ProfileController.java          # Biometrics & BMI Settings
        │   │   └── AdminController.java            # Admin Operations & Management
        │   ├── entity/                             # [Rubric 1] Domain Entities & Encapsulation
        │   │   ├── User.java                       # User Entity
        │   │   ├── Workout.java                    # Workout Entity
        │   │   ├── FitnessGoal.java                # Goal Entity
        │   │   ├── Challenge.java                  # Challenge Entity
        │   │   ├── ChallengeParticipant.java       # Participant Entity
        │   │   ├── FitnessContent.java             # Educational Content Entity
        │   │   ├── SystemSetting.java              # Settings Entity
        │   │   └── ActivityLog.java                # Audit Log Entity
        │   ├── exception/                          # [Rubric 1] Custom Exception Handling
        │   │   ├── FitTrackException.java          # Base Custom Exception
        │   │   ├── InvalidFitnessDataException.java# Validation Exception
        │   │   ├── ResourceNotFoundException.java  # 404 Exception
        │   │   └── GlobalExceptionHandler.java     # @ControllerAdvice Global Handler
        │   ├── jdbc/                               # [Rubric 4] Pure JDBC Database Operations
        │   │   ├── FitTrackJdbcOperations.java     # Pure JDBC Operations Interface
        │   │   ├── FitTrackJdbcDaoImpl.java        # PreparedStatement, ResultSet, Transactions
        │   │   └── JdbcConnectionFactory.java      # Pure Connection Management & Pooling
        │   ├── repository/                         # Spring Data JPA Repositories
        │   │   ├── UserRepository.java
        │   │   ├── WorkoutRepository.java
        │   │   ├── FitnessGoalRepository.java
        │   │   ├── ChallengeRepository.java
        │   │   ├── ChallengeParticipantRepository.java
        │   │   ├── FitnessContentRepository.java
        │   │   ├── SystemSettingRepository.java
        │   │   └── ActivityLogRepository.java
        │   ├── security/                           # Spring Security 6 & RBAC
        │   │   ├── CustomUserDetails.java
        │   │   ├── CustomUserDetailsService.java
        │   │   └── SecurityConfig.java
        │   ├── service/                            # Business Logic Services
        │   │   ├── UserService.java
        │   │   ├── WorkoutService.java
        │   │   ├── FitnessGoalService.java
        │   │   ├── ChallengeService.java
        │   │   ├── FitnessContentService.java
        │   │   ├── SystemSettingService.java
        │   │   └── ActivityLogService.java
        │   └── servlet/                            # [Rubric 5] Servlets, Filters & Listeners
        │       ├── DirectMetricsServlet.java       # Pure HttpServlet (/metrics/live)
        │       ├── AuditLoggingFilter.java         # Jakarta Filter for Request Auditing
        │       ├── FitTrackSessionListener.java    # HttpSessionListener for Session Counting
        │       └── ServletConfig.java              # Servlet Component Registration
        └── resources/
            ├── application.properties              # Main Spring Boot Configuration
            ├── application-mysql.properties        # External MySQL Configuration
            ├── static/css/custom.css               # Modern Dark Neon Styling
            └── templates/                          # Thymeleaf Responsive UI Views
                ├── auth/                           # Login & Registration Pages
                ├── user/                           # User Dashboard, Workouts, Goals, Challenges
                ├── admin/                          # Administrative Dashboard & Control Center
                └── layout/                         # Reusable Navigation & Footer Fragments
```

---

## 🌐 Live Live Endpoints for Academic Demonstration

When testing or demonstrating to your professor/evaluator:

| Endpoint | Type | Purpose |
|---|---|---|
| `http://localhost:8080/login` | Web UI | Multi-user login with instant 1-click test fill buttons |
| `http://localhost:8080/dashboard` | Web UI | Personal analytics, Chart.js visuals, BMI calculator |
| `http://localhost:8080/workouts` | Web UI | Workout CRUD operations, calorie computations |
| `http://localhost:8080/goals` | Web UI | Goal progress tracking and milestone completion |
| `http://localhost:8080/challenges` | Web UI | Browse challenges, join community events, view leaderboards |
| `http://localhost:8080/content` | Web UI | Fitness guides and moderation queue |
| `http://localhost:8080/admin/dashboard` | Web UI | Admin analytics, user management, and moderation queue |
| `http://localhost:8080/metrics/live` | **Pure Servlet** | Live JSON output directly from `DirectMetricsServlet` showing active sessions & memory stats |

---

## 📤 How to Upload to Your GitHub Repository

We have provided a fully automated script: **`push_to_github.bat`**.

1. Go to [GitHub.com](https://github.com) and click **"New Repository"**.
2. Give it a name (e.g. `fittrack`) and make sure **NOT** to initialize with a README, .gitignore, or license (leave it completely empty).
3. Copy your repository URL (e.g. `https://github.com/your-username/fittrack.git`).
4. In the `fittrack` folder, double-click **`push_to_github.bat`**.
5. Paste your repository URL and press **Enter**.
6. That's it! Your entire repository with all code, history, and documentation will be pushed to GitHub!

---

## 📜 Academic Declaration & Acknowledgements
This project was developed by **Ansh** (Team Leader), **Aryan Bhardwaj**, and **Aryaman Singh** as a comprehensive submission for the Java Full-Stack & Object-Oriented Software Engineering Evaluation. All rights reserved.
