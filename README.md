# FITTRACK: Online Fitness Tracking System

**Full-Stack Java Web Application for Fitness Management**

### 👥 Project Team
- **Group Leader**: Ansh – `25SCSE1180706` (B.Tech Computer Science & Engineering — AI & ML)
- **Group Member**: Aryan Bhardwaj – `25SCSE1180192`
- **Group Member**: Aryaman Singh – `25SCSE1180867`

---

## 🚀 Quick Start Instructions

To run the application locally on Windows:
1. Double click **`run.bat`** inside `C:\Users\LOQ\.gemini\antigravity\scratch\fittrack\`
   OR run in PowerShell / Command Prompt:
   ```powershell
   cd C:\Users\LOQ\.gemini\antigravity\scratch\fittrack
   .\run.bat
   ```
2. Open your web browser and navigate to:
   ```
   http://localhost:8080/
   ```

---

## 🔑 Pre-Configured Demo Accounts (1-Click Fill on Login Screen)

| Role | Username / Email | Password | Access Rights |
|---|---|---|---|
| **Administrator** | `admin` or `admin@fittrack.com` | `admin123` | Full Admin Dashboard, User Management, Challenge Creation, Content Review & Approvals, System Audit Logs, Global Settings |
| **End-User (Leader)** | `ansh` or `ansh@fittrack.com` | `password123` | Workout Logging, Fitness Goals, Challenge Enrolment, Article Submission, Profile & BMI Tracking |
| **End-User (Member)** | `aryan` or `aryan@fittrack.com` | `password123` | Personal Dashboard, Calisthenics Workouts, Progress Leaderboard |
| **End-User (Member)** | `aryaman` or `aryaman@fittrack.com` | `password123` | Powerlifting Logs, Challenge Progress, Content Submissions |

---

## 🛠️ Technology Stack (Matching Presentation Slide 5)
- **Backend**: Java 17/21, Spring Boot 3.3.4, Spring Security 6, Spring Data JPA, Hibernate ORM
- **Frontend**: Thymeleaf, HTML5, CSS3, Modern Dark Neon Theme (`#0b0f19` & `#ccff00`), Bootstrap 5, Chart.js
- **Database**: H2 (In-Memory / File Persistent with MySQL Compatibility mode) + MySQL Connector (`application-mysql.properties`)
- **Build Tool**: Apache Maven 3.9.9

---

## 🏛️ System Architecture & Package Structure (Slide 6 & 7)
- `com.fittrack.config`: System data initialization (`DataInitializer.java`)
- `com.fittrack.controller`: `AuthController`, `DashboardController`, `WorkoutController`, `FitnessGoalController`, `ChallengeController`, `FitnessContentController`, `ProfileController`, `AdminController`
- `com.fittrack.entity`:
  1. `User` (table: `users`)
  2. `Workout` (table: `workouts`)
  3. `FitnessGoal` (table: `fitness_goals`)
  4. `Challenge` (table: `challenges`)
  5. `ChallengeParticipant` (table: `challenge_participants`)
  6. `FitnessContent` (table: `fitness_content`)
  7. `SystemSetting` (table: `system_settings`)
  8. `ActivityLog` (table: `activity_logs`)
- `com.fittrack.repository`: JPA Repositories with custom queries for statistics and audit trails
- `com.fittrack.security`: Spring Security 6 configuration, BCrypt hashing, `CustomUserDetails`, `CustomUserDetailsService`
- `com.fittrack.service`: Business service layer managing transaction boundaries and system events

---

## 📋 Implemented Core Features (Slide 4)

### 🏃 Role: End-User Features
1. **User Registration & Login**: BCrypt secured authentication with auto-redirect based on role.
2. **User Profile Management**: Biometric parameters (age, weight, height, fitness level, bio), automatic dynamic BMI calculation.
3. **Workout Management**: Log, view, edit, and delete workouts with duration, calories burned, sport categories, intensity levels, and performance notes.
4. **Fitness Goal Management**: Set measurable targets (target value vs current value, target dates), real-time progress percentage bars, mark achieved milestones.
5. **Fitness Challenges**: Browse community challenges with target metrics, difficulty badges, and unlockable reward badges.
6. **Challenge Progress Tracking**: Join challenges, log incremental progress, and climb the real-time participant leaderboard.
7. **Fitness Content Submission**: Submit workout guides, diet protocols, and training tips into the editorial moderation queue.
8. **View Fitness Content**: Browse and read approved guides categorized by Nutrition, Workout, Recovery, and Motivation.

### 🛡️ Role: Administrator Features
1. **Registered User Management**: View all user accounts, inspect stats, suspend or reactivate accounts, promote or demote user roles.
2. **Challenge Creation & Management**: Create new community challenges, set start/end dates, targets, rewards, toggle active/inactive status, or delete.
3. **Challenge Participant Management**: Inspect all enrolled participants per challenge with completion percentages.
4. **Participant Progress Monitoring**: Real-time rank leaderboard monitoring.
5. **Fitness Content Approval & Rejection**: Review submitted community articles, approve for publication, or reject with custom editorial feedback notes.
6. **System Activity Monitoring**: Audit log trail capturing user logins, registrations, workout additions, challenge joins, and administrative actions with IP and timestamps.
7. **System Configuration Settings**: Edit application title, broadcast announcements, registration toggles, and maintenance mode.
