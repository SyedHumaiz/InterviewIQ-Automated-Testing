package com.interviewiq.stepdefinitions;

import com.interviewiq.config.ConfigManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigurationSmokeSteps {

    @Given("the InterviewIQ environment configuration is loaded")
    public void theInterviewIqEnvironmentConfigurationIsLoaded() {
        assertNotNull(ConfigManager.getCandidateBaseUrl());
    }

    @Then("the candidate application base URL should be configured")
    public void theCandidateApplicationBaseUrlShouldBeConfigured() {
        assertConfigured(ConfigManager.getCandidateBaseUrl());
    }

    @Then("the company application base URL should be configured")
    public void theCompanyApplicationBaseUrlShouldBeConfigured() {
        assertConfigured(ConfigManager.getCompanyBaseUrl());
    }

    @Then("the admin application base URL should be configured")
    public void theAdminApplicationBaseUrlShouldBeConfigured() {
        assertConfigured(ConfigManager.getAdminBaseUrl());
    }

    private void assertConfigured(String url) {
        assertNotNull(url);
        assertFalse(url.isBlank());
    }
}
