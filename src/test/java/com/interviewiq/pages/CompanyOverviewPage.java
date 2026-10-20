package com.interviewiq.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

public class CompanyOverviewPage {

    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(10);
    private static final String OVERVIEW_PATH = "/app";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By overviewHeading = By.xpath("//h1[normalize-space()='Overview']");

    public CompanyOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, PAGE_LOAD_TIMEOUT);
    }

    public void waitForLoaded() {
        wait.until(webDriver -> OVERVIEW_PATH.equals(
                URI.create(webDriver.getCurrentUrl()).getPath()));
        wait.until(ExpectedConditions.visibilityOfElementLocated(overviewHeading));
    }

    public boolean isAtOverviewPage() {
        return OVERVIEW_PATH.equals(URI.create(driver.getCurrentUrl()).getPath());
    }

    public boolean isOverviewHeadingDisplayed() {
        return driver.findElement(overviewHeading).isDisplayed();
    }
}
