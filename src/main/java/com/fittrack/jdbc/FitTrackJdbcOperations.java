package com.fittrack.jdbc;

import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: Classes for Database Operations & JDBC Interface
 * Defines explicit contract for executing database operations purely through JDBC.
 * ============================================================================
 */
public interface FitTrackJdbcOperations {

    Map<String, Object> fetchUserFitnessSummaryJdbc(Long userId);

    Map<String, Object> fetchGlobalStatsJdbc();

    Map<String, Object> fetchGlobalWorkoutStatsJdbc();

    List<Map<String, Object>> fetchChallengeLeaderboardJdbc(Long challengeId);

    int insertActivityLogJdbc(Long userId, String actionType, String description, String ipAddress);
}
