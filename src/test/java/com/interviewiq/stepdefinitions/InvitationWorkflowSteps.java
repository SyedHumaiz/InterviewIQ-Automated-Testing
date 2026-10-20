package com.interviewiq.stepdefinitions;

import com.interviewiq.pages.CandidateInvitationPage;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvitationWorkflowSteps {
    private CandidateInvitationPage invitationPage;

    @When("an anonymous candidate opens an invalid invitation token")
    public void openInvalidInvitation() {
        invitationPage = new CandidateInvitationPage(DriverManager.getDriver());
        invitationPage.openInvalidToken();
    }

    @Then("the public invitation unavailable state is displayed")
    public void invitationUnavailableDisplayed() {
        assertTrue(invitationPage.isUnavailableWithoutDetails());
    }
}
