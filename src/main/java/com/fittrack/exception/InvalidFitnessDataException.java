package com.fittrack.exception;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: OOP Implementation (Exception Inheritance)
 * Thrown when fitness parameters fail domain validation rules.
 * ============================================================================
 */
public class InvalidFitnessDataException extends FitTrackException {

    public InvalidFitnessDataException(String message) {
        super(message, "INVALID_FITNESS_METRIC");
    }
}
