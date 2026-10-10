package com.fittrack.controller;

import com.fittrack.entity.User;
import com.fittrack.security.CustomUserDetails;
import com.fittrack.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String viewProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        model.addAttribute("user", user);
        model.addAttribute("bmi", user.calculateBmi());
        return "user/profile";
    }

    @PostMapping
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam("fullName") String fullName,
                                @RequestParam(value = "age", required = false) Integer age,
                                @RequestParam(value = "weight", required = false) Double weight,
                                @RequestParam(value = "height", required = false) Double height,
                                @RequestParam(value = "fitnessLevel", required = false) String fitnessLevel,
                                @RequestParam(value = "targetGoalSummary", required = false) String targetGoalSummary,
                                @RequestParam(value = "bio", required = false) String bio,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        userService.updateProfile(user.getId(), fullName, age, weight, height, fitnessLevel, targetGoalSummary, bio);
        redirectAttributes.addFlashAttribute("successMessage", "Profile information updated successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/password")
    public String changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New passwords do not match.");
            return "redirect:/profile";
        }

        try {
            userService.changePassword(user.getId(), oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/profile";
    }
}
