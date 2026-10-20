package com.interviewiq.stepdefinitions;

import com.interviewiq.pages.CompanyInterviewsPage;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterviewWorkflowSteps {
    private CompanyInterviewsPage interviewsPage;

    @Given("an authenticated Company user opens the Interviews page")
    public void openInterviewsPage() {
        interviewsPage = new CompanyInterviewsPage(DriverManager.getDriver());
        interviewsPage.open();
    }

    @When("the Company user assigns an eligible candidate to an active position")
    public void assignEligibleCandidate() {
        interviewsPage.assignEligibleCandidate();
    }

    @When("the Company user sends the new interview invitation")
    public void sendNewInvitation() {
        interviewsPage.sendNewInterviewInvitation();
    }

    @Then("the Interviews page heading is displayed")
    public void interviewsHeadingDisplayed() {
        assertTrue(interviewsPage.isHeadingDisplayed());
    }

    @Then("the interview session list or empty state is displayed")
    public void interviewContentDisplayed() {
        assertTrue(interviewsPage.hasListOrEmptyState());
    }

    @Then("the invitation workflow controls are available")
    public void invitationControlsAvailable() {
        assertTrue(interviewsPage.hasInvitationWorkflow());
    }

    @Then("invitation generation is confirmed")
    public void invitationGenerationConfirmed() {
        assertTrue(interviewsPage.isInvitationConfirmed());
    }
}
