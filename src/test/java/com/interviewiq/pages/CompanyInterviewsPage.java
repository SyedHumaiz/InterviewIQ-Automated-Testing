package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

/** Company interview list, assignment modal, and invitation controls. */
public class CompanyInterviewsPage {
    private static final String INTERVIEWS_PATH = "/app/interviews";
    private static final Duration TIMEOUT = Duration.ofSeconds(20);

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By heading = By.xpath("//h1[normalize-space()='Interviews']");
    private final By workflowDescription = By.xpath("//*[normalize-space()='Assign candidates to active roles and manage invitation links.']");
    private final By assignInterview = By.xpath("//button[normalize-space()='Assign interview']");
    private final By finalContent = By.xpath("//h2[normalize-space()='Interview sessions' or normalize-space()='No interviews assigned yet']");
    private final By assignmentModal = By.xpath("//div[.//h2[normalize-space()='Assign interview'] and .//form]");
    private final By candidateSelect = By.xpath("//h2[normalize-space()='Assign interview']/following::form[1]//label[starts-with(normalize-space(.), 'Candidate')]//select");
    private final By positionSelect = By.xpath("//h2[normalize-space()='Assign interview']/following::form[1]//label[starts-with(normalize-space(.), 'Position')]//select");
    private final By resumeAvailability = By.xpath("//*[normalize-space()='Resume availability']/following-sibling::*[1]");
    private final By assignmentConfirmed = By.xpath("//*[normalize-space()='Interview assigned successfully.']");
    private final By invitationConfirmed = By.xpath("//*[normalize-space()='Invitation email sent. You can also copy the link before leaving this page.']");

    public CompanyInterviewsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    public void open() {
        driver.get(URI.create(ConfigManager.getCompanyBaseUrl()).resolve(INTERVIEWS_PATH.substring(1)).toString());
        waitForLoaded();
    }

    public void waitForLoaded() {
        try {
            wait.until(webDriver -> INTERVIEWS_PATH.equals(URI.create(webDriver.getCurrentUrl()).getPath()));
            wait.until(ExpectedConditions.visibilityOfElementLocated(heading));
            wait.until(ExpectedConditions.visibilityOfElementLocated(finalContent));
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Interviews page did not load: "
                    + CompanyPageDiagnostics.snapshot(driver), exception);
        }
    }

    public boolean isHeadingDisplayed() {
        return driver.findElement(heading).isDisplayed();
    }

    public boolean hasListOrEmptyState() {
        return driver.findElement(finalContent).isDisplayed();
    }

    public boolean hasInvitationWorkflow() {
        return displayed(workflowDescription) && displayed(assignInterview);
    }

    public void assignEligibleCandidate() {
        WebElement openForm = wait.until(ExpectedConditions.elementToBeClickable(assignInterview));
        openForm.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(assignmentModal));

        Select candidates = new Select(driver.findElement(candidateSelect));
        boolean eligibleCandidateFound = false;
        for (int index = 1; index < candidates.getOptions().size(); index++) {
            candidates.selectByIndex(index);
            wait.until(webDriver -> {
                String state = driver.findElement(resumeAvailability).getText();
                return !state.contains("Checking") && !state.contains("Choose a candidate");
            });
            if (driver.findElement(resumeAvailability).getText().contains("Parsed resume ready")) {
                eligibleCandidateFound = true;
                break;
            }
        }
        if (!eligibleCandidateFound) {
            throw new IllegalStateException("No active candidate with a parsed resume is available for interview assignment");
        }

        Select positions = new Select(driver.findElement(positionSelect));
        if (positions.getOptions().size() < 2) {
            throw new IllegalStateException("No active position is available for interview assignment");
        }
        positions.selectByIndex(1);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//h2[normalize-space()='Assign interview']/following::form[1]//button[normalize-space()='Assign interview']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(assignmentConfirmed));
    }

    public void sendNewInterviewInvitation() {
        By sendInvitation = By.xpath("//tr[.//*[normalize-space()='Interview assigned successfully.']]"
                + "//button[normalize-space()='Send invitation']");
        wait.until(ExpectedConditions.elementToBeClickable(sendInvitation)).click();
    }

    public boolean isInvitationConfirmed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(invitationConfirmed)).isDisplayed();
    }

    private boolean displayed(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }
}
