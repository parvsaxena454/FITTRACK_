package com.fittrack.controller;

import com.fittrack.entity.User;
import com.fittrack.entity.Workout;
import com.fittrack.security.CustomUserDetails;
import com.fittrack.service.UserService;
import com.fittrack.service.WorkoutService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;

    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

    @GetMapping
    public String listWorkouts(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        List<Workout> workouts = workoutService.getUserWorkouts(user);
        Map<String, Object> stats = workoutService.getUserWorkoutStats(user);

        model.addAttribute("workouts", workouts);
        model.addAttribute("stats", stats);
        model.addAttribute("user", user);
        return "user/workouts";
    }

    @GetMapping("/new")
    public String newWorkoutForm(Model model) {
        model.addAttribute("today", LocalDate.now());
        return "user/workout-form";
    }

    @PostMapping("/new")
    public String saveWorkout(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam("title") String title,
                              @RequestParam("workoutType") String workoutType,
                              @RequestParam("durationMinutes") Integer durationMinutes,
                              @RequestParam("caloriesBurned") Integer caloriesBurned,
                              @RequestParam("workoutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workoutDate,
                              @RequestParam("intensity") String intensity,
                              @RequestParam(value = "notes", required = false) String notes,
                              RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        workoutService.logWorkout(user, title, workoutType, durationMinutes, caloriesBurned, workoutDate, intensity, notes);
        redirectAttributes.addFlashAttribute("successMessage", "Workout '" + title + "' logged successfully! Keep up the momentum!");
        return "redirect:/workouts";
    }

    @GetMapping("/edit/{id}")
    public String editWorkoutForm(@PathVariable("id") Long id,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  Model model) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        Workout workout = workoutService.getWorkoutById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found"));

        if (!workout.getUser().getId().equals(user.getId())) {
            return "redirect:/workouts";
        }

        model.addAttribute("workout", workout);
        return "user/workout-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateWorkout(@PathVariable("id") Long id,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam("title") String title,
                                @RequestParam("workoutType") String workoutType,
                                @RequestParam("durationMinutes") Integer durationMinutes,
                                @RequestParam("caloriesBurned") Integer caloriesBurned,
                                @RequestParam("workoutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workoutDate,
                                @RequestParam("intensity") String intensity,
                                @RequestParam(value = "notes", required = false) String notes,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        workoutService.updateWorkout(id, user, title, workoutType, durationMinutes, caloriesBurned, workoutDate, intensity, notes);
        redirectAttributes.addFlashAttribute("successMessage", "Workout updated successfully.");
        return "redirect:/workouts";
    }

    @PostMapping("/delete/{id}")
    public String deleteWorkout(@PathVariable("id") Long id,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getUser().getId()).orElse(userDetails.getUser());
        workoutService.deleteWorkout(id, user);
        redirectAttributes.addFlashAttribute("successMessage", "Workout entry deleted.");
        return "redirect:/workouts";
    }
}
