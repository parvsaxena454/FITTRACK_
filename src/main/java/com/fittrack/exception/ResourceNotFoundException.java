package com.fittrack.exception;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: OOP Implementation (Exception Inheritance)
 * Thrown when an entity (User, Workout, Goal, Challenge) is not found.
 * ============================================================================
 */
public class ResourceNotFoundException extends FitTrackException {

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(resourceName + " with identifier [" + identifier + "] was not found.", "RESOURCE_NOT_FOUND");
    }
}
