package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdminCompaniesPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By heading = By.xpath("//h1[normalize-space()='Companies']");
    private final By register = By.xpath("//a[normalize-space()='Register company']");
    private final By formHeading = By.xpath("//h1[normalize-space()='Register company']");
    private final By error = By.cssSelector(".inline-error");
    private final By companyTable = By.cssSelector("table.management-table");
    private final By emptyState = By.xpath("//h3[normalize-space()='No companies found']");
    private final By search = By.cssSelector(".filters label.search input");
    private final By statusFilter = By.xpath("//div[contains(@class,'filters')]//select"
            + "[.//option[normalize-space()='Active'] and .//option[normalize-space()='Suspended']]");
    private final By sort = By.xpath("//div[contains(@class,'filters')]//select"
            + "[.//option[normalize-space()='Newest first'] and .//option[normalize-space()='Company name']]");

    public AdminCompaniesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    public void open() {
        driver.get(ConfigManager.getAdminBaseUrl() + "app/companies");
        wait.until(ExpectedConditions.visibilityOfElementLocated(heading));
    }

    public boolean displayed() { return driver.findElement(heading).isDisplayed(); }
    public boolean filtersDisplayed() {
        return driver.findElement(search).isDisplayed()
                && driver.findElements(By.cssSelector(".filters select")).size() == 3;
    }

    public boolean listOrEmptyStateDisplayed() {
        try {
            return wait.until(webDriver -> visible(companyTable) || visible(emptyState));
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Companies page did not reach a content state: "
                    + contentDiagnostics(), exception);
        }
    }

    public String contentDiagnostics() {
        boolean headingShown = visible(heading);
        boolean searchShown = visible(search);
        int selects = driver.findElements(By.cssSelector(".filters select")).size();
        boolean table = visible(companyTable);
        boolean empty = visible(emptyState);
        String body = driver.findElements(By.tagName("body")).stream().filter(WebElement::isDisplayed)
                .map(WebElement::getText).collect(Collectors.joining(" ")).replaceAll("\\s+", " ").trim();
        return "Companies state: heading=" + headingShown + ", search=" + searchShown + ", filters=" + selects
                + ", table=" + table + ", empty=" + empty + ", url=" + driver.getCurrentUrl()
                + ", body=" + body.substring(0, Math.min(1200, body.length()));
    }

    public void openRegistration() {
        driver.findElement(register).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(formHeading));
    }

    public boolean registrationDisplayed() { return driver.findElement(formHeading).isDisplayed(); }
    public boolean registrationFieldsDisplayed() {
        return driver.findElements(By.cssSelector(".form-layout input")).size() >= 10
                && driver.findElements(By.cssSelector(".form-layout select")).size() == 1;
    }
    public void submitBlankRegistration() {
        driver.findElement(By.xpath("//button[normalize-space()='Register company']")).click();
    }
    public String errorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(error)).getText();
    }

    public void searchForNonexistentCompany() {
        WebElement input = driver.findElement(search);
        input.clear();
        input.sendKeys("no-such-company-" + UUID.randomUUID());
        wait.until(ExpectedConditions.visibilityOfElementLocated(emptyState));
    }

    public boolean noResultsDisplayed() { return driver.findElement(emptyState).isDisplayed(); }
    public void selectActiveStatus() {
        new Select(driver.findElement(statusFilter)).selectByVisibleText("Active");
    }
    public boolean activeStatusSelected() {
        return "Active".equals(new Select(driver.findElement(statusFilter)).getFirstSelectedOption().getText());
    }
    public void sortByCompanyName() {
        new Select(driver.findElement(sort)).selectByVisibleText("Company name");
    }
    public boolean companyNameSortSelected() {
        return "Company name".equals(new Select(driver.findElement(sort)).getFirstSelectedOption().getText());
    }

    private boolean visible(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }
}
