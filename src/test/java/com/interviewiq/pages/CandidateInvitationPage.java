package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

/** Public candidate invitation states. */
public class CandidateInvitationPage {
    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By unavailableHeading = By.xpath("//h1[normalize-space()='Invitation unavailable']");
    private final By safeGuidance = By.xpath("//*[normalize-space()='Please contact the hiring team if you need a new invitation link.']");

    public CandidateInvitationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    public void openInvalidToken() {
        String path = "/invite/not-a-real-ui-automation-token";
        driver.get(URI.create(ConfigManager.getCandidateBaseUrl()).resolve(path.substring(1)).toString());
        wait.until(ExpectedConditions.visibilityOfElementLocated(unavailableHeading));
    }

    public boolean isUnavailableWithoutDetails() {
        return driver.findElement(unavailableHeading).isDisplayed()
                && driver.findElement(safeGuidance).isDisplayed();
    }
}
