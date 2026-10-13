package com.fittrack.config;

import com.fittrack.entity.*;
import com.fittrack.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final FitnessGoalRepository fitnessGoalRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipantRepository participantRepository;
    private final FitnessContentRepository fitnessContentRepository;
    private final SystemSettingRepository systemSettingRepository;
    private final ActivityLogRepository activityLogRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           WorkoutRepository workoutRepository,
                           FitnessGoalRepository fitnessGoalRepository,
                           ChallengeRepository challengeRepository,
                           ChallengeParticipantRepository participantRepository,
                           FitnessContentRepository fitnessContentRepository,
                           SystemSettingRepository systemSettingRepository,
                           ActivityLogRepository activityLogRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
        this.fitnessGoalRepository = fitnessGoalRepository;
        this.challengeRepository = challengeRepository;
        this.participantRepository = participantRepository;
        this.fitnessContentRepository = fitnessContentRepository;
        this.systemSettingRepository = systemSettingRepository;
        this.activityLogRepository = activityLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Data already initialized
        }

        System.out.println("=========================================================================");
        System.out.println("       FitTrack: Online Fitness Tracking System");
        System.out.println("       Group Leader:  Ansh Kataria");
        System.out.println("       Group Members: Parv Saxena, Anand Soni, Aniket Raj");
        System.out.println("=========================================================================");
        System.out.println(">>> Initializing FitTrack Seed Data...");

        // 1. Seed Users
        // Admin
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@fittrack.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("FitTrack Administrator");
        admin.setRole("ROLE_ADMIN");
        admin.setEnabled(true);
        admin.setAge(28);
        admin.setWeight(76.0);
        admin.setHeight(180.0);
        admin.setFitnessLevel("Advanced");
        admin.setBio("System Administrator and Lead Fitness Director at FitTrack.");
        admin = userRepository.save(admin);

        // Group Leader: Ansh Kataria
        User ansh = new User();
        ansh.setUsername("ansh");
        ansh.setEmail("ansh@fittrack.com");
        ansh.setPassword(passwordEncoder.encode("password123"));
        ansh.setFullName("Ansh Kataria");
        ansh.setRole("ROLE_USER");
        ansh.setEnabled(true);
        ansh.setAge(21);
        ansh.setWeight(74.5);
        ansh.setHeight(178.0);
        ansh.setFitnessLevel("Advanced");
        ansh.setTargetGoalSummary("Hypertrophy & Marathon Endurance");
        ansh.setBio("B.Tech CSE - AI/ML. Group Leader: Ansh Kataria. Fitness enthusiast, marathon runner, and weightlifting advocate.");
        ansh = userRepository.save(ansh);

        // Group Member 1: Parv Saxena
        User parv = new User();
        parv.setUsername("parv");
        parv.setEmail("parv@fittrack.com");
        parv.setPassword(passwordEncoder.encode("password123"));
        parv.setFullName("Parv Saxena");
        parv.setRole("ROLE_USER");
        parv.setEnabled(true);
        parv.setAge(21);
        parv.setWeight(82.0);
        parv.setHeight(182.0);
        parv.setFitnessLevel("Intermediate");
        parv.setTargetGoalSummary("Powerlifting & Strength Development");
        parv.setBio("Group Member. Focusing on progressive overload and the Big Three: Squat, Bench, and Deadlift.");
        parv = userRepository.save(parv);

        // Group Member 2: Anand Soni
        User anand = new User();
        anand.setUsername("anand");
        anand.setEmail("anand@fittrack.com");
        anand.setPassword(passwordEncoder.encode("password123"));
        anand.setFullName("Anand Soni");
        anand.setRole("ROLE_USER");
        anand.setEnabled(true);
        anand.setAge(21);
        anand.setWeight(68.0);
        anand.setHeight(174.0);
        anand.setFitnessLevel("Intermediate");
        anand.setTargetGoalSummary("Calisthenics & Lean Mass");
        anand.setBio("Group Member. B.Tech CSE student passionate about bodyweight mastery, calisthenics, and agility.");
        anand = userRepository.save(anand);

        // Group Member 3: Aniket Raj
        User aniket = new User();
        aniket.setUsername("aniket");
        aniket.setEmail("aniket@fittrack.com");
        aniket.setPassword(passwordEncoder.encode("password123"));
        aniket.setFullName("Aniket Raj");
        aniket.setRole("ROLE_USER");
        aniket.setEnabled(true);
        aniket.setAge(21);
        aniket.setWeight(71.0);
        aniket.setHeight(176.0);
        aniket.setFitnessLevel("Advanced");
        aniket.setTargetGoalSummary("Sub-40 10K & Metabolic Conditioning");
        aniket.setBio("Group Member. Dedicated to long-distance running, interval cardiovascular conditioning, and HIIT.");
        aniket = userRepository.save(aniket);

        // 2. Seed Workouts
        workoutRepository.save(new Workout(ansh, "Chest & Triceps Hypertrophy", "Strength", 65, 480, LocalDate.now().minusDays(1), "HIGH", "Bench press 4x8 @ 85kg, Incline DB press 3x10, Dips 3x12."));
        workoutRepository.save(new Workout(ansh, "Morning 8km Aerobic Run", "Cardio", 42, 520, LocalDate.now().minusDays(2), "MEDIUM", "Pace 5:15 min/km. Kept heart rate in Zone 3 throughout."));
        workoutRepository.save(new Workout(ansh, "HIIT Core & Tabata Sprint", "HIIT", 30, 360, LocalDate.now().minusDays(3), "HIGH", "Battle ropes, kettlebell swings, burpees 40s on / 20s off."));
        workoutRepository.save(new Workout(ansh, "Leg Day Heavy Squats", "Strength", 70, 560, LocalDate.now().minusDays(5), "HIGH", "Back squat 5x5 @ 110kg, Romanian deadlifts 4x10, Bulgarian split squats."));
        workoutRepository.save(new Workout(ansh, "Post-Run Yoga Recovery", "Yoga", 35, 140, LocalDate.now().minusDays(6), "LOW", "Hamstring stretches, hip openers, and spine mobility work."));

        workoutRepository.save(new Workout(anand, "Upper Body Calisthenics", "Strength", 50, 380, LocalDate.now().minusDays(1), "MEDIUM", "Muscle-up drills, weighted pull-ups +10kg, ring push-ups."));
        workoutRepository.save(new Workout(anand, "5km Evening Park Run", "Cardio", 26, 310, LocalDate.now().minusDays(3), "MEDIUM", "Fast paced tempo run through central park."));

        workoutRepository.save(new Workout(parv, "Deadlift PR Session", "Strength", 75, 610, LocalDate.now().minusDays(2), "HIGH", "Deadlifts worked up to 160kg triple. Barbell rows 4x8."));
        workoutRepository.save(new Workout(parv, "Stairmaster & Core Burn", "Cardio", 35, 340, LocalDate.now().minusDays(4), "MEDIUM", "Level 8 steady pace, hanging leg raises."));

        workoutRepository.save(new Workout(aniket, "Interval 10km Track Run", "Cardio", 48, 550, LocalDate.now().minusDays(1), "HIGH", "10x400m intervals at 3:45 min/km pace with 60s jog recoveries."));
        workoutRepository.save(new Workout(aniket, "Kettlebell Conditioning & Plyo", "HIIT", 35, 410, LocalDate.now().minusDays(3), "HIGH", "Box jumps, kettlebell cleans, wall balls."));

        // 3. Seed Fitness Goals
        fitnessGoalRepository.save(new FitnessGoal(ansh, "Reach 72 kg Target Weight", "Weight Loss", 72.0, 74.5, "kg", LocalDate.now().minusDays(14), LocalDate.now().plusDays(30), "Gradual sustainable fat loss cutting phase."));
        fitnessGoalRepository.save(new FitnessGoal(ansh, "Run 10km Sub-48 Minutes", "Running Distance", 10.0, 8.0, "km", LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), "Preparing for the city spring 10k race."));
        fitnessGoalRepository.save(new FitnessGoal(ansh, "Burn 3,500 kcal Weekly from Workouts", "Weekly Workouts", 3500.0, 2060.0, "kcal", LocalDate.now().minusDays(4), LocalDate.now().plusDays(3), "Active weekly expenditure target."));

        fitnessGoalRepository.save(new FitnessGoal(anand, "Achieve 15 Clean Muscle-Ups", "Muscle Gain", 15.0, 9.0, "reps", LocalDate.now().minusDays(15), LocalDate.now().plusDays(45), "Master strict bar muscle-ups with clean form."));
        fitnessGoalRepository.save(new FitnessGoal(parv, "Hit 180kg Deadlift 1RM", "Powerlifting", 180.0, 160.0, "kg", LocalDate.now().minusDays(20), LocalDate.now().plusDays(40), "Targeting personal deadlift milestone."));
        fitnessGoalRepository.save(new FitnessGoal(aniket, "Complete Half-Marathon under 1h 45m", "Running Distance", 21.1, 15.0, "km", LocalDate.now().minusDays(12), LocalDate.now().plusDays(35), "Target half-marathon pace conditioning."));

        // 4. Seed Challenges
        Challenge c1 = new Challenge("30-Day Core & Abs Blitz", "Transform your core strength with 30 consecutive days of focused abdominal exercises, planks, and stability training.", "Strength", "Intermediate", 30.0, "days", LocalDate.now().minusDays(5), LocalDate.now().plusDays(25), "Core Titan");
        Challenge c2 = new Challenge("100km Cycling Centurion", "Clock 100 kilometers of road or stationary cycling this month. Boost cardiovascular endurance and stamina.", "Cardio", "Advanced", 100.0, "km", LocalDate.now().minusDays(7), LocalDate.now().plusDays(23), "Centurion Rider");
        Challenge c3 = new Challenge("Daily 10,000 Steps Grind", "Hit the gold standard of daily movement! Accumulate 14 consecutive days of 10k steps for metabolic vitality.", "Consistency", "Beginner", 14.0, "days", LocalDate.now().minusDays(2), LocalDate.now().plusDays(12), "Stride Master");
        Challenge c4 = new Challenge("HIIT Calorie Inferno", "Burn a combined total of 4,000 active calories through high intensity interval training sessions.", "Cardio", "Elite", 4000.0, "kcal", LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), "Inferno Champion");

        c1 = challengeRepository.save(c1);
        c2 = challengeRepository.save(c2);
        c3 = challengeRepository.save(c3);
        c4 = challengeRepository.save(c4);

        // 5. Seed Challenge Participants
        ChallengeParticipant cp1 = new ChallengeParticipant(ansh, c1);
        cp1.setProgressValue(18.0);
        cp1.recalculateProgress();
        participantRepository.save(cp1);

        ChallengeParticipant cp2 = new ChallengeParticipant(ansh, c2);
        cp2.setProgressValue(68.5);
        cp2.recalculateProgress();
        participantRepository.save(cp2);

        ChallengeParticipant cp3 = new ChallengeParticipant(anand, c1);
        cp3.setProgressValue(22.0);
        cp3.recalculateProgress();
        participantRepository.save(cp3);

        ChallengeParticipant cp4 = new ChallengeParticipant(parv, c1);
        cp4.setProgressValue(14.0);
        cp4.recalculateProgress();
        participantRepository.save(cp4);

        ChallengeParticipant cp5 = new ChallengeParticipant(anand, c3);
        cp5.setProgressValue(14.0);
        cp5.recalculateProgress(); // 100% completed
        participantRepository.save(cp5);

        ChallengeParticipant cp6 = new ChallengeParticipant(aniket, c2);
        cp6.setProgressValue(82.0);
        cp6.recalculateProgress();
        participantRepository.save(cp6);

        ChallengeParticipant cp7 = new ChallengeParticipant(aniket, c4);
        cp7.setProgressValue(2850.0);
        cp7.recalculateProgress();
        participantRepository.save(cp7);

        // 6. Seed Fitness Content
        FitnessContent art1 = new FitnessContent(ansh,
                "The Science of Progressive Overload in Strength Training",
                "Workout Guide",
                "Understand the physiological triggers behind muscle hypertrophy and neurological adaptations through structured tension progression.",
                "To stimulate continual muscle adaptation and strength gains, muscles must experience loads beyond what they are accustomed to. Progressive overload is achieved not solely by adding iron to the barbell, but also by manipulating training volume (sets x reps), time under tension, mechanical disadvantage, and reducing inter-set rest intervals. Track your lifts weekly in FitTrack to verify upward trends."
        );
        art1.setStatus("APPROVED");
        art1.setReviewedBy("admin");
        art1.setReviewedAt(LocalDateTime.now().minusDays(2));
        art1.setAdminFeedback("Comprehensive and scientifically sound. Approved for featured publication.");
        fitnessContentRepository.save(art1);

        FitnessContent art2 = new FitnessContent(anand,
                "Nutritional Timing: Optimizing Fuel Before and After High-Intensity Sessions",
                "Nutrition & Diet",
                "A practical roadmap to pre-workout carbohydrate availability and post-workout glycogen synthesis combined with rapid amino acid uptake.",
                "Consuming a low-glycemic carbohydrate meal 2-3 hours prior to demanding workouts sustains blood glucose without insulin spikes. In the post-exercise window, a 3:1 ratio of fast-digesting carbohydrates to complete proteins stimulates maximal muscle protein synthesis and replenishes depleted glycogen stores. Always ensure adequate sodium and magnesium for peak muscle contraction."
        );
        art2.setStatus("APPROVED");
        art2.setReviewedBy("admin");
        art2.setReviewedAt(LocalDateTime.now().minusDays(1));
        art2.setAdminFeedback("Excellent actionable guide for our members.");
        fitnessContentRepository.save(art2);

        FitnessContent art3 = new FitnessContent(parv,
                "The Crucial Role of Sleep Architecture in Athletic Recovery",
                "Recovery & Sleep",
                "How deep slow-wave sleep and REM cycles regulate growth hormone release, cortisol suppression, and tissue repair.",
                "Human Growth Hormone (HGH) secretion peaks during slow-wave stage 3 deep sleep. Athletes who sleep fewer than 7 hours exhibit higher injury rates, compromised reaction velocity, and blunted anabolic recovery. Prioritize consistent sleep wake cycles and dark, cool sleeping environments."
        );
        art3.setStatus("PENDING");
        fitnessContentRepository.save(art3);

        FitnessContent art4 = new FitnessContent(aniket,
                "Aerobic Base Conditioning & Lactate Threshold Optimization",
                "Workout Guide",
                "Zone 2 training protocols for mitochondrial density expansion and sustained lactate buffering efficiency.",
                "Zone 2 aerobic base building enables athletes to sustain higher outputs while relying predominantly on fatty acid oxidation, preserving glycogen for high-intensity surges. Consistent weekly volume at conversational effort produces left ventricular eccentric hypertrophy, boosting stroke volume."
        );
        art4.setStatus("APPROVED");
        art4.setReviewedBy("admin");
        art4.setReviewedAt(LocalDateTime.now().minusHours(12));
        art4.setAdminFeedback("Superb endurance protocol article.");
        fitnessContentRepository.save(art4);

        // 7. Seed System Settings
        systemSettingRepository.save(new SystemSetting("APP_TITLE", "FitTrack - Online Fitness Tracking System", "Application brand title"));
        systemSettingRepository.save(new SystemSetting("ALLOW_REGISTRATION", "true", "Controls whether new end-users can register"));
        systemSettingRepository.save(new SystemSetting("MAINTENANCE_MODE", "false", "Puts the system in maintenance mode"));
        systemSettingRepository.save(new SystemSetting("MOTD_ANNOUNCEMENT", "Welcome to FitTrack! Log your workouts, join challenges, and connect with fitness enthusiasts.", "Dashboard header message"));

        // 8. Seed Activity Logs
        activityLogRepository.save(new ActivityLog(admin, "LOGIN", "Admin authenticated successfully", "127.0.0.1"));
        activityLogRepository.save(new ActivityLog(ansh, "LOGIN", "User @ansh logged in", "127.0.0.1"));
        activityLogRepository.save(new ActivityLog(ansh, "WORKOUT_LOGGED", "Logged Strength workout: 'Chest & Triceps Hypertrophy'", "127.0.0.1"));
        activityLogRepository.save(new ActivityLog(anand, "LOGIN", "User @anand logged in", "127.0.0.1"));
        activityLogRepository.save(new ActivityLog(parv, "LOGIN", "User @parv logged in", "127.0.0.1"));
        activityLogRepository.save(new ActivityLog(aniket, "LOGIN", "User @aniket logged in", "127.0.0.1"));
        activityLogRepository.save(new ActivityLog(admin, "CONTENT_APPROVED", "Admin approved article 'The Science of Progressive Overload'", "127.0.0.1"));

        System.out.println(">>> FitTrack Seed Data Successfully Loaded!");
    }
}
