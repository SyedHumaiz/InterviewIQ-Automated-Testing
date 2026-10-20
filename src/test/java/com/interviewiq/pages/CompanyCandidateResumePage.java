package com.interviewiq.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

/** Candidate-profile resume controls exposed by the authenticated Company application. */
public class CompanyCandidateResumePage {
    private static final Duration TIMEOUT = Duration.ofSeconds(20);

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By viewCandidateDetails = By.xpath("//a[normalize-space()='View candidate details']");
    private final By resumeSection = By.xpath("//section[.//h2[normalize-space()='Resume']]");
    private final By resumeInput = By.xpath("//section[.//h2[normalize-space()='Resume']]//input[@type='file']");
    private final By removeResume = By.cssSelector("button[aria-label='Remove resume']");
    private final By emptyState = By.xpath("//*[normalize-space()='No resume uploaded yet']");
    private final By nonPdfError = By.xpath("//*[normalize-space()='Choose a PDF resume that is 5 MB or smaller.']");
    private final By oversizedError = By.xpath("//*[normalize-space()='Resume files must be 5 MB or smaller.']");
    private final By uploadingState = By.xpath("//*[contains(normalize-space(), 'Uploading and parsing resume')]");

    public CompanyCandidateResumePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    public void openFromCreationConfirmation() {
        wait.until(ExpectedConditions.elementToBeClickable(viewCandidateDetails)).click();
        waitForLoaded();
    }

    public void waitForLoaded() {
        try {
            wait.until(webDriver -> URI.create(webDriver.getCurrentUrl()).getPath()
                    .matches("/app/candidates/[^/]+"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(resumeSection));
            wait.until(webDriver -> hasResume() || hasEmptyState());
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Candidate resume area did not load: "
                    + CompanyPageDiagnostics.snapshot(driver), exception);
        }
    }

    public boolean isResumeAreaDisplayed() {
        return driver.findElement(resumeSection).isDisplayed();
    }

    public boolean hasResume() {
        return displayed(removeResume);
    }

    public boolean hasEmptyState() {
        return displayed(emptyState);
    }

    public boolean acceptsPdfFiles() {
        String accept = driver.findElement(resumeInput).getAttribute("accept");
        return accept != null && accept.toLowerCase().contains("pdf");
    }

    public void upload(String absolutePath) {
        driver.findElement(resumeInput).sendKeys(absolutePath);
    }

    public void uploadAndWaitForResume(String absolutePath) {
        upload(absolutePath);
        wait.until(webDriver -> !displayed(uploadingState) && hasResume());
    }

    public void remove() {
        wait.until(ExpectedConditions.elementToBeClickable(removeResume)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emptyState));
    }

    public boolean isNonPdfErrorDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(nonPdfError)).isDisplayed();
    }

    public boolean isOversizedErrorDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(oversizedError)).isDisplayed();
    }

    public void refresh() {
        driver.navigate().refresh();
        waitForLoaded();
    }

    private boolean displayed(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }
}
