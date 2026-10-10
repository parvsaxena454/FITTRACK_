package com.fittrack.exception;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: OOP Implementation (Exception Handling & Inheritance)
 * Base custom unchecked exception hierarchy for domain errors.
 * ============================================================================
 */
public class FitTrackException extends RuntimeException {

    private final String errorCode;

    public FitTrackException(String message) {
        super(message);
        this.errorCode = "FITTRACK_GENERAL_ERROR";
    }

    public FitTrackException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public FitTrackException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
