package com.fittrack.controller;

import com.fittrack.entity.Challenge;
import com.fittrack.entity.ChallengeParticipant;
import com.fittrack.entity.User;
import com.fittrack.security.CustomUserDetails;
import com.fittrack.service.ChallengeService;
import com.fittrack.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final UserService userService;

    public ChallengeController(ChallengeService challengeService, UserService userService) {
        this.challengeService = challengeService;
        this.userService = userService;
    }

    @GetMapping
    public String browseChallenges(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        List<Challenge> activeChallenges = challengeService.getActiveChallenges();
        List<ChallengeParticipant> myParticipations = challengeService.getUserParticipations(user);

        model.addAttribute("challenges", activeChallenges);
        model.addAttribute("myParticipations", myParticipations);
        model.addAttribute("user", user);
        return "user/challenges";
    }

    @GetMapping("/{id}")
    public String viewChallenge(@PathVariable("id") Long id,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                Model model) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        Challenge challenge = challengeService.getChallengeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

        Optional<ChallengeParticipant> userParticipation = challengeService.getUserParticipation(user, challenge);
        List<ChallengeParticipant> leaderboard = challengeService.getChallengeLeaderboard(challenge);

        model.addAttribute("challenge", challenge);
        model.addAttribute("userParticipation", userParticipation.orElse(null));
        model.addAttribute("leaderboard", leaderboard);
        model.addAttribute("user", user);
        return "user/challenge-detail";
    }

    @PostMapping("/{id}/join")
    public String joinChallenge(@PathVariable("id") Long id,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        try {
            challengeService.joinChallenge(user, id);
            redirectAttributes.addFlashAttribute("successMessage", "You have successfully joined the challenge! Let's conquer it!");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/challenges/" + id;
    }

    @PostMapping("/participant/{participantId}/progress")
    public String updateProgress(@PathVariable("participantId") Long participantId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam("progressDelta") Double progressDelta,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        try {
            ChallengeParticipant updated = challengeService.updateParticipantProgress(participantId, user, progressDelta);
            if ("COMPLETED".equalsIgnoreCase(updated.getStatus())) {
                redirectAttributes.addFlashAttribute("successMessage", "Incredible! You completed the challenge and unlocked the '" + updated.getChallenge().getRewardBadge() + "' badge! 🏅");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Progress updated! Current completion: " + updated.getProgressPercentage() + "%");
            }
            return "redirect:/challenges/" + updated.getChallenge().getId();
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/challenges";
        }
    }
}
