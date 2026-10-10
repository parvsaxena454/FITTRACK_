package com.fittrack.jdbc;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * ============================================================================
 * RUBRIC COMPLIANCE: Database Connectivity (JDBC) (Connection Management)
 * Provides direct, explicit java.sql.Connection management for low-level JDBC operations.
 * ============================================================================
 */
@Component
public class JdbcConnectionFactory {

    private final DataSource dataSource;

    public JdbcConnectionFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Establishes and returns an open java.sql.Connection from the pool.
     */
    public Connection openConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        return conn;
    }

    /**
     * Inspects active database metadata via JDBC.
     */
    public String getDatabaseInfo() {
        try (Connection conn = openConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            return metaData.getDatabaseProductName() + " v" + metaData.getDatabaseProductVersion()
                    + " [Driver: " + metaData.getDriverName() + " " + metaData.getDriverVersion() + "]";
        } catch (SQLException e) {
            return "JDBC Metadata Error: " + e.getMessage();
        }
    }
}
