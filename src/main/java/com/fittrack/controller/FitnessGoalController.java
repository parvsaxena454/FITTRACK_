package com.fittrack.controller;

import com.fittrack.entity.FitnessGoal;
import com.fittrack.entity.User;
import com.fittrack.security.CustomUserDetails;
import com.fittrack.service.FitnessGoalService;
import com.fittrack.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/goals")
public class FitnessGoalController {

    private final FitnessGoalService fitnessGoalService;
    private final UserService userService;

    public FitnessGoalController(FitnessGoalService fitnessGoalService, UserService userService) {
        this.fitnessGoalService = fitnessGoalService;
        this.userService = userService;
    }

    @GetMapping
    public String listGoals(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        List<FitnessGoal> goals = fitnessGoalService.getUserGoals(user);
        long activeCount = fitnessGoalService.getActiveGoalsCount(user);
        long achievedCount = fitnessGoalService.getAchievedGoalsCount(user);

        model.addAttribute("goals", goals);
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("achievedCount", achievedCount);
        model.addAttribute("user", user);
        return "user/goals";
    }

    @GetMapping("/new")
    public String newGoalForm(Model model) {
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("defaultTargetDate", LocalDate.now().plusMonths(1));
        return "user/goal-form";
    }

    @PostMapping("/new")
    public String saveGoal(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @RequestParam("title") String title,
                           @RequestParam("category") String category,
                           @RequestParam("targetValue") Double targetValue,
                           @RequestParam("currentValue") Double currentValue,
                           @RequestParam("unit") String unit,
                           @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                           @RequestParam("targetDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
                           @RequestParam(value = "description", required = false) String description,
                           RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        fitnessGoalService.createGoal(user, title, category, targetValue, currentValue, unit, startDate != null ? startDate : LocalDate.now(), targetDate, description);
        redirectAttributes.addFlashAttribute("successMessage", "Fitness goal '" + title + "' set successfully!");
        return "redirect:/goals";
    }

    @PostMapping("/{id}/progress")
    public String updateProgress(@PathVariable("id") Long id,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam("currentValue") Double currentValue,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        FitnessGoal goal = fitnessGoalService.updateProgress(id, user, currentValue);
        if ("ACHIEVED".equalsIgnoreCase(goal.getStatus())) {
            redirectAttributes.addFlashAttribute("successMessage", "Congratulations! Goal '" + goal.getTitle() + "' marked as ACHIEVED! 🏆");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Progress updated for '" + goal.getTitle() + "'.");
        }
        return "redirect:/goals";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable("id") Long id,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               @RequestParam("status") String status,
                               RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        FitnessGoal goal = fitnessGoalService.getGoalById(id).orElseThrow();
        fitnessGoalService.updateGoal(id, user, goal.getTitle(), goal.getCategory(), goal.getTargetValue(), goal.getCurrentValue(), goal.getUnit(), goal.getTargetDate(), status, goal.getDescription());
        redirectAttributes.addFlashAttribute("successMessage", "Goal status updated to " + status + ".");
        return "redirect:/goals";
    }

    @PostMapping("/{id}/delete")
    public String deleteGoal(@PathVariable("id") Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        fitnessGoalService.deleteGoal(id, user);
        redirectAttributes.addFlashAttribute("successMessage", "Fitness goal removed.");
        return "redirect:/goals";
    }
}
