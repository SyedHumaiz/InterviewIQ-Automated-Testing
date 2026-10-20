package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

/** Company profile and interview-default settings. */
public class CompanySettingsPage {
    private static final String SETTINGS_PATH = "/app/settings";
    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By heading = By.xpath("//h1[normalize-space()='Settings']");
    private final By companyProfile = By.xpath("//h2[normalize-space()='Company profile']");
    private final By interviewDefaults = By.xpath("//h2[normalize-space()='Interview defaults']");
    private final By companyName = By.cssSelector("input#company-name");
    private final By expiryDays = By.cssSelector("input#expiry-days[type='number']");
    private final By timezone = By.cssSelector("select#timezone");
    private final By feedbackVisibility = By.cssSelector("input#feedback-visibility[type='checkbox']");

    public CompanySettingsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    public void open() {
        driver.get(URI.create(ConfigManager.getCompanyBaseUrl()).resolve(SETTINGS_PATH.substring(1)).toString());
        try {
            wait.until(webDriver -> SETTINGS_PATH.equals(URI.create(webDriver.getCurrentUrl()).getPath()));
            wait.until(ExpectedConditions.visibilityOfElementLocated(heading));
            wait.until(ExpectedConditions.visibilityOfElementLocated(companyName));
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Company Settings did not load: "
                    + CompanyPageDiagnostics.snapshot(driver), exception);
        }
    }

    public boolean isHeadingDisplayed() { return driver.findElement(heading).isDisplayed(); }

    public boolean areSettingsSectionsDisplayed() {
        return displayed(companyProfile) && displayed(interviewDefaults)
                && displayed(companyName) && displayed(expiryDays)
                && displayed(timezone) && displayed(feedbackVisibility);
    }

    public void enterExpiryDays(String value) {
        WebElement input = driver.findElement(expiryDays);
        input.clear();
        input.sendKeys(value);
    }

    public boolean isExpiryValid() {
        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].validity.valid;", driver.findElement(expiryDays));
    }

    private boolean displayed(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }
}
