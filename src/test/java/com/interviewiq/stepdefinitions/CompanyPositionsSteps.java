package com.interviewiq.stepdefinitions;

import com.interviewiq.pages.CompanyPositionsPage;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyPositionsSteps {
    private CompanyPositionsPage positionsPage;
    private String title;
    private String updatedTitle;
    private final String department = "Automation Engineering";
    private final String description = "Automation-owned position used only for Company Position Management testing.";
    private final String skills = "Java, Selenium, Cucumber";

    @Given("an authenticated Company user opens the Positions page")
    public void openPositions() {
        positionsPage = new CompanyPositionsPage(DriverManager.getDriver());
        positionsPage.open();
    }

    @When("the Company user opens the New position form") public void openForm() { positionsPage.openNewPositionForm(); }
    @When("the Company user submits the blank Position form") public void submitBlank() { positionsPage.submit(); }
    @When("the Company user submits a Position without a department") public void submitWithoutDepartment() { positionsPage.enterTitle("Automation Position"); positionsPage.submit(); }
    @When("the Company user enters a one-character Position title") public void enterShortTitle() { positionsPage.enterTitle("A"); positionsPage.submit(); }
    @When("the Company user submits a Position with zero interview questions") public void submitZeroQuestions() { fillValidFields(); positionsPage.enterQuestionCount("0"); positionsPage.submit(); }
    @When("the Company user enters a short Position description") public void enterShortDescription() { positionsPage.enterDescription("Short"); positionsPage.submit(); }
    @When("the Company user creates a unique automation Position") public void createPosition() { createIdentity(); positionsPage.createPosition(title, department, description, skills); }
    @When("the Company user opens the created Position for editing") public void openEdit() { positionsPage.editPosition(title); }
    @When("the Company user updates the created Position title") public void updateTitle() { updatedTitle = title + " Updated"; positionsPage.saveEdit(title, updatedTitle); title = updatedTitle; }
    @When("the Company user archives the created Position") public void archive() { positionsPage.archivePosition(title); }

    @Then("the Positions page heading is displayed") public void heading() { assertTrue(positionsPage.isHeadingDisplayed()); }
    @Then("the New position control is available") public void newControl() { assertTrue(positionsPage.isNewPositionAvailable()); }
    @Then("Position cards or the legitimate empty state are displayed") public void cardsOrEmpty() { assertTrue(positionsPage.hasCardsOrEmptyState()); }
    @Then("the New position form is displayed") public void newForm() { assertTrue(positionsPage.isNewPositionFormDisplayed()); }
    @Then("the Position title fails browser validation") public void invalidTitle() { assertFalse(positionsPage.isTitleValid()); }
    @Then("the Position department fails browser validation") public void invalidDepartment() { assertFalse(positionsPage.isDepartmentValid()); }
    @Then("the Position question count fails browser validation") public void invalidQuestionCount() { assertFalse(positionsPage.isQuestionCountValid()); }
    @Then("the Position description fails browser validation") public void invalidDescription() { assertFalse(positionsPage.isDescriptionValid()); }
    @Then("the created Position is visible") public void positionVisible() { assertTrue(positionsPage.isPositionVisible(title)); }
    @Then("the created Position is Active") public void positionActive() { assertTrue(positionsPage.hasActiveStatus(title)); }
    @Then("the created Position displays its automation description") public void hasDescription() { assertTrue(positionsPage.displaysDescription(title, description)); }
    @Then("the created Position displays all submitted skills") public void hasSkills() { assertTrue(positionsPage.displaysSkill(title, "Java") && positionsPage.displaysSkill(title, "Selenium") && positionsPage.displaysSkill(title, "Cucumber")); }
    @Then("the Edit position form is displayed") public void editForm() { assertTrue(positionsPage.isEditFormDisplayed()); }
    @Then("the updated Position title is visible") public void updatedVisible() { assertTrue(positionsPage.isPositionVisible(updatedTitle)); }
    @Then("the created Position is Archived") public void archived() { assertTrue(positionsPage.hasArchivedStatus(title)); }

    private void createIdentity() { title = "Automation Position " + UUID.randomUUID().toString().substring(0, 8); }
    private void fillValidFields() { positionsPage.enterTitle("Automation Position"); positionsPage.enterDepartment(department); positionsPage.enterDescription(description); positionsPage.enterSkills(skills); }
}
