package com.franchise.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.sql.*;

@Configuration
public class SchemaValidationConfig {
    @Value("${schema.validation.jdbc-url}")
    private String jdbcUrl;

    @Value("${schema.validation.username}")
    private String username;

    @Value("${schema.validation.password}")
    private String password;

    @EventListener(ApplicationReadyEvent.class)
    public void validateSchema() {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement()) {

            stmt.executeQuery("SELECT 1 FROM franquicia LIMIT 1");
            stmt.executeQuery("SELECT 1 FROM sucursal LIMIT 1");
            stmt.executeQuery("SELECT 1 FROM producto LIMIT 1");

        } catch (SQLException e) {
            throw new RuntimeException("Error al validar las tablas: " + e.getMessage(), e);
        }
    }
}
