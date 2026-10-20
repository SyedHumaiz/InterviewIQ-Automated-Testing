package com.interviewiq.stepdefinitions;

import com.interviewiq.pages.CompanyCandidatesPage;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyCandidatesSteps {
    private CompanyCandidatesPage candidatesPage;
    private String candidateName;
    private String candidateEmail;

    @Given("an authenticated Company user opens the Candidates page")
    public void anAuthenticatedCompanyUserOpensTheCandidatesPage() {
        candidatesPage = new CompanyCandidatesPage(DriverManager.getDriver());
        candidatesPage.open();
    }

    @When("the Company user opens the Add candidate form")
    public void theCompanyUserOpensTheAddCandidateForm() { candidatesPage.openAddCandidateForm(); }

    @When("the Company user submits the candidate form without a name")
    public void theCompanyUserSubmitsTheCandidateFormWithoutAName() { candidatesPage.submitCandidate(); }

    @When("the Company user submits the candidate form without an email")
    public void theCompanyUserSubmitsTheCandidateFormWithoutAnEmail() { candidatesPage.enterFullName("Automation Candidate"); candidatesPage.submitCandidate(); }

    @When("the Company user enters an invalid candidate email")
    public void theCompanyUserEntersAnInvalidCandidateEmail() { candidatesPage.enterFullName("Automation Candidate"); candidatesPage.enterEmail("not-an-email"); candidatesPage.submitCandidate(); }

    @When("the Company user submits otherwise valid candidate data without a resume")
    public void theCompanyUserSubmitsOtherwiseValidCandidateDataWithoutAResume() { createIdentity(); candidatesPage.enterFullName(candidateName); candidatesPage.enterEmail(candidateEmail); candidatesPage.submitCandidate(); }

    @When("the Company user creates a unique automation candidate")
    public void theCompanyUserCreatesAUniqueAutomationCandidate() { createIdentity(); candidatesPage.createCandidate(candidateName, candidateEmail, resumePath()); }

    @When("the Company user returns to the candidate listing")
    public void theCompanyUserReturnsToTheCandidateListing() { candidatesPage.returnToCandidates(); }

    @When("the Company user searches for the created candidate")
    public void theCompanyUserSearchesForTheCreatedCandidate() { candidatesPage.searchFor(candidateEmail); }

    @When("the Company user searches for a nonexistent candidate")
    public void theCompanyUserSearchesForANonexistentCandidate() { candidatesPage.searchFor("no-such-automation-candidate-" + UUID.randomUUID()); }

    @When("the Company user filters candidates by {string}")
    public void theCompanyUserFiltersCandidatesBy(String status) { candidatesPage.filterBy(status); }

    @When("the Company user archives the created candidate")
    public void theCompanyUserArchivesTheCreatedCandidate() { candidatesPage.archiveCandidate(candidateName); }

    @Then("the Candidates page heading is displayed")
    public void theCandidatesPageHeadingIsDisplayed() { assertTrue(candidatesPage.isHeadingDisplayed()); }

    @Then("the Add candidate control is available")
    public void theAddCandidateControlIsAvailable() { assertTrue(candidatesPage.isAddCandidateAvailable()); }

    @Then("the candidate list or its empty state is displayed")
    public void theCandidateListOrItsEmptyStateIsDisplayed() { assertTrue(candidatesPage.hasListOrEmptyState()); }

    @Then("the Add candidate form is displayed")
    public void theAddCandidateFormIsDisplayed() { assertTrue(candidatesPage.isAddCandidateFormDisplayed()); }

    @Then("the candidate name fails browser validation")
    public void theCandidateNameFailsBrowserValidation() { assertFalse(candidatesPage.isFullNameValid()); }

    @Then("the candidate email fails browser validation")
    public void theCandidateEmailFailsBrowserValidation() { assertFalse(candidatesPage.isEmailValid()); }

    @Then("candidate creation is rejected with the missing resume message")
    public void candidateCreationIsRejectedWithTheMissingResumeMessage() { assertTrue(candidatesPage.isMissingResumeErrorDisplayed()); }

    @Then("candidate creation is confirmed for the created candidate")
    public void candidateCreationIsConfirmedForTheCreatedCandidate() { assertTrue(candidatesPage.isCreationConfirmedFor(candidateName)); }

    @Then("the created candidate is visible in the listing")
    public void theCreatedCandidateIsVisibleInTheListing() { assertTrue(candidatesPage.isCandidateVisible(candidateName)); }

    @Then("the created candidate is not visible in the listing")
    public void theCreatedCandidateIsNotVisibleInTheListing() { assertFalse(candidatesPage.isCandidateVisible(candidateName)); }

    @Then("no candidates match the search")
    public void noCandidatesMatchTheSearch() { assertTrue(candidatesPage.isNoMatchingCandidatesDisplayed()); }

    @Then("the created candidate is shown as archived")
    public void theCreatedCandidateIsShownAsArchived() { assertTrue(candidatesPage.isCandidateArchived(candidateName)); }

    private void createIdentity() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        candidateName = "Automation Candidate " + suffix;
        candidateEmail = "automation.candidate." + suffix + "@example.com";
    }

    private String resumePath() {
        try {
            return Path.of(getClass().getClassLoader().getResource("fixtures/minimal-resume.pdf").toURI()).toAbsolutePath().toString();
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("Could not resolve test resume fixture", exception);
        }
    }
}
