package com.interviewiq.stepdefinitions;

import com.interviewiq.pages.CompanySettingsPage;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanySettingsSteps {
    private CompanySettingsPage settingsPage;

    @Given("an authenticated Company user opens Settings")
    public void openSettings() {
        settingsPage = new CompanySettingsPage(DriverManager.getDriver());
        settingsPage.open();
    }

    @When("the Company user enters an invitation expiry below the supported range")
    public void enterUnsupportedExpiry() {
        settingsPage.enterExpiryDays("0");
    }

    @Then("the Company Settings heading is displayed")
    public void settingsHeadingDisplayed() {
        assertTrue(settingsPage.isHeadingDisplayed());
    }

    @Then("the Company settings sections are displayed")
    public void settingsSectionsDisplayed() {
        assertTrue(settingsPage.areSettingsSectionsDisplayed());
    }

    @Then("the invitation expiry fails browser validation")
    public void expiryFailsValidation() {
        assertFalse(settingsPage.isExpiryValid());
    }
}
