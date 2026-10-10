package com.fittrack.controller;

import com.fittrack.entity.ChallengeParticipant;
import com.fittrack.entity.FitnessGoal;
import com.fittrack.entity.User;
import com.fittrack.entity.Workout;
import com.fittrack.security.CustomUserDetails;
import com.fittrack.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final UserService userService;
    private final WorkoutService workoutService;
    private final FitnessGoalService fitnessGoalService;
    private final ChallengeService challengeService;
    private final SystemSettingService systemSettingService;

    public DashboardController(UserService userService,
                               WorkoutService workoutService,
                               FitnessGoalService fitnessGoalService,
                               ChallengeService challengeService,
                               SystemSettingService systemSettingService) {
        this.userService = userService;
        this.workoutService = workoutService;
        this.fitnessGoalService = fitnessGoalService;
        this.challengeService = challengeService;
        this.systemSettingService = systemSettingService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId())
                .orElse(userDetails.getUser());

        Map<String, Object> stats = workoutService.getUserWorkoutStats(user);
        List<Workout> recentWorkouts = workoutService.getUserWorkouts(user);
        List<FitnessGoal> goals = fitnessGoalService.getUserGoals(user);
        List<ChallengeParticipant> participations = challengeService.getUserParticipations(user);

        model.addAttribute("user", user);
        model.addAttribute("stats", stats);
        model.addAttribute("recentWorkouts", recentWorkouts.stream().limit(5).toList());
        model.addAttribute("goals", goals.stream().limit(4).toList());
        model.addAttribute("participations", participations.stream().limit(3).toList());
        model.addAttribute("motd", systemSettingService.getSetting("MOTD_ANNOUNCEMENT", "Keep pushing towards your peak potential!"));
        model.addAttribute("bmi", user.calculateBmi());

        return "user/dashboard";
    }
}
