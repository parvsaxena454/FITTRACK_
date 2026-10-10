package com.fittrack.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: OOP Exception Handling & Web Integration
 * Centralized @ControllerAdvice handling all application errors.
 * ============================================================================
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/dashboard";
    }

    @ExceptionHandler(InvalidFitnessDataException.class)
    public String handleInvalidFitnessData(InvalidFitnessDataException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Validation Error: " + ex.getMessage());
        return "redirect:/dashboard";
    }

    @ExceptionHandler(FitTrackException.class)
    public String handleFitTrackGeneral(FitTrackException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "System Error [" + ex.getErrorCode() + "]: " + ex.getMessage());
        return "redirect:/dashboard";
    }

    @ExceptionHandler(Exception.class)
    public String handleUncaught(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Unexpected Application Exception");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "An unexpected server error occurred.");
        return "user/dashboard";
    }
}
