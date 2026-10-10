package com.fittrack.controller;

import com.fittrack.entity.User;
import com.fittrack.service.ChallengeService;
import com.fittrack.service.FitnessContentService;
import com.fittrack.service.SystemSettingService;
import com.fittrack.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;
    private final ChallengeService challengeService;
    private final FitnessContentService fitnessContentService;
    private final SystemSettingService systemSettingService;

    public AuthController(UserService userService,
                          ChallengeService challengeService,
                          FitnessContentService fitnessContentService,
                          SystemSettingService systemSettingService) {
        this.userService = userService;
        this.challengeService = challengeService;
        this.fitnessContentService = fitnessContentService;
        this.systemSettingService = systemSettingService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            return isAdmin ? "redirect:/admin/dashboard" : "redirect:/dashboard";
        }

        model.addAttribute("appTitle", systemSettingService.getSetting("APP_TITLE", "FitTrack - Online Fitness Tracking System"));
        model.addAttribute("motd", systemSettingService.getSetting("MOTD_ANNOUNCEMENT", "Track workouts, conquer fitness challenges, and smash your goals!"));
        model.addAttribute("activeChallenges", challengeService.getActiveChallenges());
        model.addAttribute("featuredArticles", fitnessContentService.getApprovedContent());
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/dashboard";
        }
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/dashboard";
        }
        String allowReg = systemSettingService.getSetting("ALLOW_REGISTRATION", "true");
        if (!"true".equalsIgnoreCase(allowReg)) {
            model.addAttribute("errorMessage", "Registration is currently disabled by administrator.");
        }
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam("username") String username,
                                      @RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      @RequestParam("fullName") String fullName,
                                      RedirectAttributes redirectAttributes) {
        String allowReg = systemSettingService.getSetting("ALLOW_REGISTRATION", "true");
        if (!"true".equalsIgnoreCase(allowReg)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New registrations are currently closed.");
            return "redirect:/register";
        }

        try {
            userService.registerUser(username, email, password, fullName);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! You can now log in with your credentials.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/register";
        }
    }
}
