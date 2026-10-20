package com.interviewiq.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Platform audit record table and its legitimate empty state. */
public class AdminAuditPage {
    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By heading = By.xpath("//h1[normalize-space()='Audit']");
    private final By table = By.cssSelector("table.management-table.audit-table");
    private final By emptyState = By.xpath("//h3[normalize-space()='No audit events yet']");

    public AdminAuditPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    public void waitForLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(heading));
            wait.until(webDriver -> displayed(table) || displayed(emptyState));
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Admin Audit did not reach a content state at "
                    + driver.getCurrentUrl(), exception);
        }
    }

    public boolean hasContentState() { return displayed(table) || displayed(emptyState); }

    private boolean displayed(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }
}
