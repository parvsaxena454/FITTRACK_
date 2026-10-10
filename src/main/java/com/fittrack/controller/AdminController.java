package com.fittrack.controller;

import com.fittrack.entity.Challenge;
import com.fittrack.entity.FitnessContent;
import com.fittrack.entity.User;
import com.fittrack.security.CustomUserDetails;
import com.fittrack.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final WorkoutService workoutService;
    private final ChallengeService challengeService;
    private final FitnessContentService fitnessContentService;
    private final ActivityLogService activityLogService;
    private final SystemSettingService systemSettingService;

    public AdminController(UserService userService,
                           WorkoutService workoutService,
                           ChallengeService challengeService,
                           FitnessContentService fitnessContentService,
                           ActivityLogService activityLogService,
                           SystemSettingService systemSettingService) {
        this.userService = userService;
        this.workoutService = workoutService;
        this.challengeService = challengeService;
        this.fitnessContentService = fitnessContentService;
        this.activityLogService = activityLogService;
        this.systemSettingService = systemSettingService;
    }

    // 1. Admin Dashboard Overview
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User admin = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());

        model.addAttribute("admin", admin);
        model.addAttribute("totalUsers", userService.getTotalUsersCount());
        model.addAttribute("totalWorkouts", workoutService.getTotalWorkoutsCount());
        model.addAttribute("totalChallenges", challengeService.getTotalChallengesCount());
        model.addAttribute("pendingReviews", fitnessContentService.getPendingCount());
        model.addAttribute("recentLogs", activityLogService.getRecentLogs().stream().limit(10).toList());
        model.addAttribute("pendingArticles", fitnessContentService.getPendingContent());
        return "admin/dashboard";
    }

    // 2. Registered User Management
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable("id") Long id,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   RedirectAttributes redirectAttributes) {
        boolean active = userService.toggleUserStatus(id, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("successMessage", "User account status changed to: " + (active ? "ACTIVE" : "SUSPENDED"));
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String updateUserRole(@PathVariable("id") Long id,
                                 @RequestParam("role") String role,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        userService.updateUserRole(id, role, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("successMessage", "User role updated to " + role);
        return "redirect:/admin/users";
    }

    // 3. Challenge Creation & Management
    @GetMapping("/challenges")
    public String manageChallenges(Model model) {
        List<Challenge> challenges = challengeService.getAllChallenges();
        model.addAttribute("challenges", challenges);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("defaultEndDate", LocalDate.now().plusDays(30));
        return "admin/challenges";
    }

    @PostMapping("/challenges/new")
    public String createChallenge(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @RequestParam("title") String title,
                                  @RequestParam("description") String description,
                                  @RequestParam("category") String category,
                                  @RequestParam("difficulty") String difficulty,
                                  @RequestParam("targetMetric") Double targetMetric,
                                  @RequestParam("targetUnit") String targetUnit,
                                  @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                  @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                  @RequestParam(value = "rewardBadge", required = false) String rewardBadge,
                                  RedirectAttributes redirectAttributes) {
        User admin = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        challengeService.createChallenge(title, description, category, difficulty, targetMetric, targetUnit, startDate, endDate, rewardBadge, admin);
        redirectAttributes.addFlashAttribute("successMessage", "New fitness challenge '" + title + "' launched successfully!");
        return "redirect:/admin/challenges";
    }

    @PostMapping("/challenges/{id}/toggle")
    public String toggleChallenge(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boolean active = challengeService.toggleChallengeActive(id);
        redirectAttributes.addFlashAttribute("successMessage", "Challenge status changed to: " + (active ? "ACTIVE" : "INACTIVE"));
        return "redirect:/admin/challenges";
    }

    @PostMapping("/challenges/{id}/delete")
    public String deleteChallenge(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        challengeService.deleteChallenge(id);
        redirectAttributes.addFlashAttribute("successMessage", "Challenge removed successfully.");
        return "redirect:/admin/challenges";
    }

    // 4. Challenge Participants & Progress Monitoring
    @GetMapping("/challenges/{id}/participants")
    public String viewParticipants(@PathVariable("id") Long id, Model model) {
        Challenge challenge = challengeService.getChallengeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));
        model.addAttribute("challenge", challenge);
        model.addAttribute("participants", challengeService.getChallengeLeaderboard(challenge));
        return "admin/challenge-participants";
    }

    // 5. Fitness Content Review (Approval/Rejection)
    @GetMapping("/content")
    public String manageContent(Model model) {
        model.addAttribute("pendingList", fitnessContentService.getPendingContent());
        model.addAttribute("allList", fitnessContentService.getAllContent());
        return "admin/content";
    }

    @PostMapping("/content/{id}/approve")
    public String approveContent(@PathVariable("id") Long id,
                                 @RequestParam(value = "feedback", required = false) String feedback,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        fitnessContentService.approveContent(id, userDetails.getUsername(), feedback);
        redirectAttributes.addFlashAttribute("successMessage", "Content approved and published to member community!");
        return "redirect:/admin/content";
    }

    @PostMapping("/content/{id}/reject")
    public String rejectContent(@PathVariable("id") Long id,
                                @RequestParam(value = "feedback", required = false) String feedback,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        fitnessContentService.rejectContent(id, userDetails.getUsername(), feedback);
        redirectAttributes.addFlashAttribute("successMessage", "Content rejected. Author has been notified with feedback.");
        return "redirect:/admin/content";
    }

    // 6. System Activity Monitoring (Audit Logs)
    @GetMapping("/activity")
    public String activityLogs(Model model) {
        model.addAttribute("logs", activityLogService.getRecentLogs());
        model.addAttribute("totalLogs", activityLogService.getTotalLogsCount());
        return "admin/activity";
    }

    // 7. System Settings Management
    @GetMapping("/settings")
    public String systemSettings(Model model) {
        model.addAttribute("settings", systemSettingService.getAllSettings());
        return "admin/settings";
    }

    @PostMapping("/settings")
    public String updateSetting(@RequestParam("settingKey") String settingKey,
                                @RequestParam("settingValue") String settingValue,
                                @RequestParam(value = "description", required = false) String description,
                                RedirectAttributes redirectAttributes) {
        systemSettingService.updateSetting(settingKey, settingValue, description);
        redirectAttributes.addFlashAttribute("successMessage", "Setting '" + settingKey + "' updated successfully.");
        return "redirect:/admin/settings";
    }
}
