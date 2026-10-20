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

/** Page object for the Company candidate list and candidate onboarding form. */
public class CompanyCandidatesPage {

    private static final Duration TIMEOUT = Duration.ofSeconds(12);
    private static final String CANDIDATES_PATH = "/app/candidates";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By heading = By.xpath("//h1[normalize-space()='Candidates']");
    private final By addCandidate = By.xpath("//button[normalize-space()='Add candidate']");
    private final By candidateTable = By.xpath("//table[.//th[normalize-space()='Candidate']]");
    private final By candidateContent = By.xpath("//table[.//th[normalize-space()='Candidate']] | //*[normalize-space()='No candidates yet']");
    private final By search = By.cssSelector("input[placeholder='Search candidates...']");
    private final By statusFilter = By.xpath("//select[.//option[normalize-space()='All candidates']]");

    private final By formHeading = By.xpath("//h1[normalize-space()='Add candidate']");
    private final By fullName = By.xpath("//label[starts-with(normalize-space(.), 'Full name')]//input");
    private final By email = By.xpath("//label[starts-with(normalize-space(.), 'Email address')]//input");
    private final By resume = By.cssSelector("input#candidate-resume[type='file']");
    private final By createCandidate = By.xpath("//button[normalize-space()='Create candidate']");
    private final By formError = By.xpath("//*[contains(normalize-space(), 'Choose a PDF resume before onboarding this candidate.')]");
    private final By createdHeading = By.xpath("//h2[normalize-space()='Candidate created']");
    private final By createdMessage = By.xpath("//h2[normalize-space()='Candidate created']/following-sibling::p");
    private final By backToCandidates = By.xpath("//a[normalize-space()='Back to candidates']");

    public CompanyCandidatesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    public void open() {
        driver.get(URI.create(ConfigManager.getCompanyBaseUrl()).resolve(CANDIDATES_PATH.substring(1)).toString());
        waitForLoaded();
    }

    public void waitForLoaded() {
        try {
            wait.until(webDriver -> CANDIDATES_PATH.equals(URI.create(webDriver.getCurrentUrl()).getPath()));
            wait.until(ExpectedConditions.visibilityOfElementLocated(heading));
            wait.until(ExpectedConditions.visibilityOfElementLocated(candidateContent));
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Candidates page did not load: " + CompanyPageDiagnostics.snapshot(driver), exception);
        }
    }

    public boolean isHeadingDisplayed() { return driver.findElement(heading).isDisplayed(); }
    public boolean isAddCandidateAvailable() { return driver.findElement(addCandidate).isDisplayed(); }
    public boolean hasListOrEmptyState() { return driver.findElement(candidateContent).isDisplayed(); }
    public boolean isTableDisplayed() { return !driver.findElements(candidateTable).isEmpty(); }

    public void openAddCandidateForm() {
        wait.until(ExpectedConditions.elementToBeClickable(addCandidate)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(formHeading));
    }

    public boolean isAddCandidateFormDisplayed() { return driver.findElement(formHeading).isDisplayed(); }

    public void enterFullName(String value) { replace(fullName, value); }
    public void enterEmail(String value) { replace(email, value); }
    public void uploadResume(String absolutePath) { driver.findElement(resume).sendKeys(absolutePath); }
    public void submitCandidate() { driver.findElement(createCandidate).click(); }
    public boolean isFullNameValid() { return isValid(fullName); }
    public boolean isEmailValid() { return isValid(email); }
    public boolean isMissingResumeErrorDisplayed() { return wait.until(ExpectedConditions.visibilityOfElementLocated(formError)).isDisplayed(); }

    public void createCandidate(String name, String candidateEmail, String resumePath) {
        enterFullName(name);
        enterEmail(candidateEmail);
        uploadResume(resumePath);
        submitCandidate();
        wait.until(ExpectedConditions.visibilityOfElementLocated(createdHeading));
    }

    public boolean isCreationConfirmedFor(String name) {
        return driver.findElement(createdHeading).isDisplayed()
                && driver.findElement(createdMessage).getText().contains(name);
    }

    public void returnToCandidates() {
        wait.until(ExpectedConditions.elementToBeClickable(backToCandidates)).click();
        waitForLoaded();
    }

    public boolean isCandidateVisible(String name) {
        return !driver.findElements(candidateRow(name)).isEmpty();
    }

    public void searchFor(String term) {
        replace(search, term);
        wait.until(driver -> !driver.findElements(By.xpath("//*[normalize-space()='No matching candidates']")).isEmpty()
                || !driver.findElements(candidateRowContaining(term)).isEmpty());
    }

    public boolean isNoMatchingCandidatesDisplayed() {
        return !driver.findElements(By.xpath("//*[normalize-space()='No matching candidates']")).isEmpty();
    }

    public void filterBy(String visibleOption) {
        new Select(driver.findElement(statusFilter)).selectByVisibleText(visibleOption);
    }

    public void archiveCandidate(String name) {
        By archive = By.xpath("//tr[.//td[contains(normalize-space(), " + xpathLiteral(name) + ")]]//button[normalize-space()='Archive']");
        wait.until(ExpectedConditions.elementToBeClickable(archive)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[.//td[contains(normalize-space(), " + xpathLiteral(name) + ")]]//*[normalize-space()='Archived']")));
    }

    public boolean isCandidateArchived(String name) {
        return !driver.findElements(By.xpath("//tr[.//td[contains(normalize-space(), " + xpathLiteral(name) + ")]]//*[normalize-space()='Archived']")).isEmpty();
    }

    private By candidateRow(String name) {
        return By.xpath("//tr[.//td[contains(normalize-space(), " + xpathLiteral(name) + ")]]");
    }

    private By candidateRowContaining(String term) {
        return By.xpath("//tr[contains(normalize-space(), " + xpathLiteral(term) + ")]");
    }

    private void replace(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    private boolean isValid(By locator) {
        return (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "return arguments[0].validity.valid;", driver.findElement(locator));
    }

    private static String xpathLiteral(String value) {
        return value.contains("'")
                ? "concat('" + value.replace("'", "', \"'\", '") + "')"
                : "'" + value + "'";
    }
}
