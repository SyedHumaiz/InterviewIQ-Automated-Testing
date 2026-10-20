package com.interviewiq.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides InterviewIQ test environment configuration from the test classpath.
 */
public final class ConfigManager {

    private static final String CONFIG_RESOURCE = "config/test.properties";
    private static final Properties PROPERTIES = loadProperties();

    private ConfigManager() {
    }

    public static String getCandidateBaseUrl() {
        return getRequiredProperty("candidate.base.url");
    }

    public static String getCompanyBaseUrl() {
        return getRequiredProperty("company.base.url");
    }

    public static String getAdminBaseUrl() {
        return getRequiredProperty("admin.base.url");
    }

    public static String getCompanyTestEmail() {
        return getRequiredSystemProperty("company.test.email");
    }

    public static String getCompanyTestPassword() {
        return getRequiredSystemProperty("company.test.password");
    }

    public static String getAdminTestEmail() {
        return getRequiredSystemProperty("admin.test.email");
    }

    public static String getAdminTestPassword() {
        return getRequiredSystemProperty("admin.test.password");
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = ConfigManager.class.getClassLoader()
                .getResourceAsStream(CONFIG_RESOURCE)) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file not found on the test classpath: " + CONFIG_RESOURCE);
            }

            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read configuration file: " + CONFIG_RESOURCE, exception);
        }
    }

    private static String getRequiredProperty(String propertyName) {
        String value = System.getProperty(propertyName);
        if (value == null) {
            value = PROPERTIES.getProperty(propertyName);
        }

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Required configuration property is missing or blank: " + propertyName);
        }

        return value.trim();
    }

    private static String getRequiredSystemProperty(String propertyName) {
        String value = System.getProperty(propertyName);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Required system property is missing or blank: " + propertyName);
        }

        return value;
    }
}
