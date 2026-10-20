package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

/** Authenticated Company navigation and logout lifecycle. */
public class CompanySessionPage {
    private static final Duration TIMEOUT = Duration.ofSeconds(12);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By logout = By.xpath("//button[.//span[normalize-space()='Log out']]");
    private final By loginHeading = By.xpath("//h2[normalize-space()='Sign in to your workspace']");

    public CompanySessionPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    public void navigateAcrossProtectedPages() {
        openAndWait("/app/candidates", "Candidates");
        openAndWait("/app/positions", "Positions");
        openAndWait("/app/settings", "Settings");
    }

    public boolean remainsAuthenticated() {
        return "/app/settings".equals(URI.create(driver.getCurrentUrl()).getPath())
                && driver.findElement(logout).isDisplayed();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logout)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginHeading));
    }

    public boolean isAtLogin() {
        return "/login".equals(URI.create(driver.getCurrentUrl()).getPath())
                && driver.findElement(loginHeading).isDisplayed();
    }

    private void openAndWait(String path, String heading) {
        driver.get(URI.create(ConfigManager.getCompanyBaseUrl()).resolve(path.substring(1)).toString());
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[normalize-space()='" + heading + "']")));
    }
}
