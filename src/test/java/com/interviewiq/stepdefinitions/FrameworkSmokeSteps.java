package com.interviewiq.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrameworkSmokeSteps {

    private boolean frameworkConfigured;

    @Given("the InterviewIQ test framework is configured")
    public void theInterviewIqTestFrameworkIsConfigured() {
        frameworkConfigured = true;
    }

    @Then("the Cucumber scenario should run successfully")
    public void theCucumberScenarioShouldRunSuccessfully() {
        assertTrue(frameworkConfigured);
    }
}
