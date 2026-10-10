package com.fittrack.controller;

import com.fittrack.entity.FitnessContent;
import com.fittrack.entity.User;
import com.fittrack.security.CustomUserDetails;
import com.fittrack.service.FitnessContentService;
import com.fittrack.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FitnessContentController {

    private final FitnessContentService fitnessContentService;
    private final UserService userService;

    public FitnessContentController(FitnessContentService fitnessContentService, UserService userService) {
        this.fitnessContentService = fitnessContentService;
        this.userService = userService;
    }

    // Public / Members: Browse approved articles
    @GetMapping("/articles")
    public String browseArticles(@RequestParam(value = "category", required = false) String category,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {
        List<FitnessContent> articles = fitnessContentService.getApprovedContent();
        if (category != null && !category.isBlank() && !"ALL".equalsIgnoreCase(category)) {
            articles = articles.stream().filter(a -> a.getCategory().equalsIgnoreCase(category)).toList();
        }

        model.addAttribute("articles", articles);
        model.addAttribute("selectedCategory", category != null ? category : "ALL");
        if (userDetails != null) {
            model.addAttribute("user", userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser()));
        }
        return "content/articles";
    }

    // Public / Members: View specific article
    @GetMapping("/articles/{id}")
    public String readArticle(@PathVariable("id") Long id,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        FitnessContent article = fitnessContentService.getContentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        model.addAttribute("article", article);
        if (userDetails != null) {
            model.addAttribute("user", userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser()));
        }
        return "content/article-detail";
    }

    // Authenticated User: Submit new article / guide
    @GetMapping("/content/submit")
    public String submitForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        model.addAttribute("user", user);
        return "content/submit-form";
    }

    @PostMapping("/content/submit")
    public String processSubmission(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestParam("title") String title,
                                    @RequestParam("category") String category,
                                    @RequestParam("summary") String summary,
                                    @RequestParam("bodyContent") String bodyContent,
                                    @RequestParam(value = "coverImageUrl", required = false) String coverImageUrl,
                                    RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        fitnessContentService.submitContent(user, title, category, summary, bodyContent, coverImageUrl);
        redirectAttributes.addFlashAttribute("successMessage",
                "Your fitness article '" + title + "' has been submitted for editorial review! Our administrators will review it shortly.");
        return "redirect:/content/my-submissions";
    }

    // Authenticated User: View my submissions
    @GetMapping("/content/my-submissions")
    public String mySubmissions(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        List<FitnessContent> mySubmissions = fitnessContentService.getUserContentSubmissions(user);

        model.addAttribute("submissions", mySubmissions);
        model.addAttribute("user", user);
        return "content/my-submissions";
    }

    // User: Delete own content
    @PostMapping("/content/{id}/delete")
    public String deleteContent(@PathVariable("id") Long id,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        boolean isAdmin = user.getRole().equals("ROLE_ADMIN");
        fitnessContentService.deleteContent(id, user, isAdmin);
        redirectAttributes.addFlashAttribute("successMessage", "Article removed successfully.");
        return "redirect:/content/my-submissions";
    }
}
