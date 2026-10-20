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
import java.util.List;
import java.util.stream.Collectors;

/** Page object for Company position cards and the create/edit position modal. */
public class CompanyPositionsPage {
    private static final Duration TIMEOUT = Duration.ofSeconds(12);
    private static final String POSITIONS_PATH = "/app/positions";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By heading = By.xpath("//h1[normalize-space()='Positions']");
    private final By newPosition = By.xpath("//button[normalize-space()='New position']");
    private final By positionContent = By.xpath("//*[normalize-space()='No positions yet'] | //h2[not(ancestor::form)]");
    private final By formHeading = By.xpath("//h2[normalize-space()='New position' or normalize-space()='Edit position']");
    private final By title = By.cssSelector("input[name='title']");
    private final By department = By.cssSelector("input[name='department']");
    private final By description = By.cssSelector("textarea[name='description']");
    private final By skills = By.cssSelector("input[name='requiredSkills']");
    private final By questionCount = By.cssSelector("input[name='questionCount']");
    private final By submit = By.xpath("//form//button[normalize-space()='Create position' or normalize-space()='Save changes']");

    public CompanyPositionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    public void open() {
        driver.get(URI.create(ConfigManager.getCompanyBaseUrl()).resolve(POSITIONS_PATH.substring(1)).toString());
        waitForLoaded();
    }

    public void waitForLoaded() {
        try {
            wait.until(webDriver -> POSITIONS_PATH.equals(URI.create(webDriver.getCurrentUrl()).getPath()));
            wait.until(ExpectedConditions.visibilityOfElementLocated(heading));
            wait.until(ExpectedConditions.visibilityOfElementLocated(positionContent));
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Positions page did not load: " + CompanyPageDiagnostics.snapshot(driver), exception);
        }
    }

    public boolean isHeadingDisplayed() { return driver.findElement(heading).isDisplayed(); }
    public boolean isNewPositionAvailable() { return driver.findElement(newPosition).isDisplayed(); }
    public boolean hasCardsOrEmptyState() { return driver.findElement(positionContent).isDisplayed(); }

    public void openNewPositionForm() {
        wait.until(ExpectedConditions.elementToBeClickable(newPosition)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(formHeading));
    }

    public boolean isNewPositionFormDisplayed() { return "New position".equals(driver.findElement(formHeading).getText()); }
    public void submit() { driver.findElement(submit).click(); }
    public void enterTitle(String value) { replace(title, value); }
    public void enterDepartment(String value) { replace(department, value); }
    public void enterDescription(String value) { replace(description, value); }
    public void enterSkills(String value) { replace(skills, value); }
    public void enterQuestionCount(String value) { replace(questionCount, value); }
    public boolean isTitleValid() { return isValid(title); }
    public boolean isDepartmentValid() { return isValid(department); }
    public boolean isQuestionCountValid() { return isValid(questionCount); }
    public boolean isDescriptionValid() { return isValid(description); }

    public void createPosition(String titleValue, String departmentValue, String descriptionValue, String skillsValue) {
        enterTitle(titleValue);
        enterDepartment(departmentValue);
        enterDescription(descriptionValue);
        enterSkills(skillsValue);
        submit();
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(formHeading));
            wait.until(ExpectedConditions.visibilityOfElementLocated(card(titleValue)));
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Position creation was not confirmed for '" + titleValue + "': "
                    + creationDiagnostics(titleValue), exception);
        }
    }

    public boolean isPositionVisible(String titleValue) { return !driver.findElements(card(titleValue)).isEmpty(); }
    public boolean hasActiveStatus(String titleValue) { return !driver.findElements(By.xpath(cardXpath(titleValue) + "//*[normalize-space()='Active']")).isEmpty(); }
    public boolean hasArchivedStatus(String titleValue) { return !driver.findElements(By.xpath(cardXpath(titleValue) + "//*[normalize-space()='Archived']")).isEmpty(); }
    public boolean displaysDescription(String titleValue, String text) { return driver.findElement(card(titleValue)).getText().contains(text); }
    public boolean displaysSkill(String titleValue, String skill) { return driver.findElement(card(titleValue)).getText().contains(skill); }

    public void editPosition(String existingTitle) {
        By edit = By.xpath(cardXpath(existingTitle) + "//button[normalize-space()='Edit']");
        wait.until(ExpectedConditions.elementToBeClickable(edit)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Edit position']")));
    }

    public boolean isEditFormDisplayed() { return !driver.findElements(By.xpath("//h2[normalize-space()='Edit position']")).isEmpty(); }

    public void saveEdit(String oldTitle, String newTitle) {
        enterTitle(newTitle);
        submit();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formHeading));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(card(oldTitle)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(card(newTitle)));
    }

    public void archivePosition(String titleValue) {
        By archive = By.xpath(cardXpath(titleValue) + "//button[normalize-space()='Archive']");
        wait.until(ExpectedConditions.elementToBeClickable(archive)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cardXpath(titleValue) + "//*[normalize-space()='Archived']")));
    }

    private By card(String titleValue) { return By.xpath(cardXpath(titleValue)); }
    private String cardXpath(String titleValue) {
        return "//section[.//h2[normalize-space()=" + literal(titleValue) + "]"
                + " and .//button[normalize-space()='Edit']]";
    }
    private void replace(By locator, String value) { WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)); element.clear(); element.sendKeys(value); }
    private boolean isValid(By locator) { return (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].validity.valid;", driver.findElement(locator)); }

    private String creationDiagnostics(String titleValue) {
        String url = driver.getCurrentUrl();
        return "url=" + url
                + ", path=" + URI.create(url).getPath()
                + ", visibleH1=" + visibleText(By.tagName("h1"))
                + ", visibleH2=" + visibleText(By.tagName("h2"))
                + ", positionFormVisible=" + isVisible(formHeading)
                + ", alerts=" + visibleText(By.cssSelector("[role='alert']"))
                + ", statusMessages=" + visibleText(By.cssSelector("[role='status']"))
                + ", titleVisibleInPage=" + driver.findElement(By.tagName("body")).getText().contains(titleValue)
                + ", editControlPresent=" + isVisible(By.xpath("//button[normalize-space()='Edit']"))
                + ", archiveControlPresent=" + isVisible(By.xpath("//button[normalize-space()='Archive']"));
    }

    private boolean isVisible(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }

    private String visibleText(By locator) {
        List<String> values = driver.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .filter(value -> !value.isBlank())
                .collect(Collectors.toList());
        return values.toString();
    }

    private static String literal(String value) {
        return value.contains("'")
                ? "concat('" + value.replace("'", "', \"'\", '") + "')"
                : "'" + value + "'";
    }
}
