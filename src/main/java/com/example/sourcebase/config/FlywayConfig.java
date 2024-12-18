package com.example.sourcebase.config;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlywayConfig {
    @Value("${spring.flyway.locations}")
    String flywayLocations;

    @Value("${spring.datasource.url}")
    String dataSourceUrl;

    @Value("${spring.datasource.username}")
    String dataSourceUsername;

    @Value("${spring.datasource.password}")
    String dataSourcePassword;

    @Bean
    public Flyway flyway() {
        createDatabaseIfNotExists();
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource())
                .locations(flywayLocations)
                .baselineOnMigrate(true)
                .load();
        flyway.repair();
        flyway.migrate();
        return flyway;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(dataSourceUsername);
        dataSource.setPassword(dataSourcePassword);
        return dataSource;
    }

    private void createDatabaseIfNotExists() {
        String baseUrl = dataSourceUrl.substring(0, dataSourceUrl.lastIndexOf('/'));
        String databaseName = dataSourceUrl.substring(dataSourceUrl.lastIndexOf('/') + 1);
        try (Connection connection = DriverManager.getConnection(baseUrl, dataSourceUsername, dataSourcePassword);
             Statement statement = connection.createStatement()) {
            String createDbQuery = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            statement.executeUpdate(createDbQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database: " + databaseName, e);
        }
    }
}

