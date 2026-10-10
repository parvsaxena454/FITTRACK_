package com.fittrack.common;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: Collections & Generics (Generic Data Transfer Object)
 * Generic response container for API and Servlet communication.
 * ============================================================================
 */
public class GenericApiResponse<T> {

    private boolean success;
    private String message;
    private T payload;
    private LocalDateTime timestamp;

    public GenericApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public GenericApiResponse(boolean success, String message, T payload) {
        this.success = success;
        this.message = message;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> GenericApiResponse<T> success(String message, T payload) {
        return new GenericApiResponse<>(true, message, payload);
    }

    public static <T> GenericApiResponse<T> failure(String message) {
        return new GenericApiResponse<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
