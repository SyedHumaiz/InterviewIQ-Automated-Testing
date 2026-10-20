package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

public class CompanyLoginPage {

    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(10);
    private static final String LOGIN_PATH = "/login";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By loginHeading = By.xpath("//h2[normalize-space()='Sign in to your workspace']");
    private final By emailField = By.cssSelector("input[type='email']");
    private final By passwordField = By.cssSelector("input[type='password']");
    private final By signInButton = By.xpath("//button[normalize-space()='Sign in']");
    private final By authenticationError = By.xpath("//div[normalize-space()='Invalid email or password.']");
    private final By loginError = By.cssSelector("div.bg-red-50");

    public CompanyLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, PAGE_LOAD_TIMEOUT);
    }

    public void open() {
        driver.get(ConfigManager.getCompanyBaseUrl());
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginHeading));
    }

    public boolean isLoginPageDisplayed() {
        return driver.findElement(loginHeading).isDisplayed();
    }

    public boolean areCredentialFieldsDisplayed() {
        return driver.findElement(emailField).isDisplayed()
                && driver.findElement(passwordField).isDisplayed();
    }

    public boolean isSignInButtonDisplayed() {
        return driver.findElement(signInButton).isDisplayed();
    }

    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickSignIn() {
        driver.findElement(signInButton).click();
    }

    public void clearCredentials() {
        driver.findElement(emailField).clear();
        driver.findElement(passwordField).clear();
    }

    public void submit() {
        clickSignIn();
    }

    public String getEmailInputType() {
        return driver.findElement(emailField).getAttribute("type");
    }

    public boolean isEmailValueValid() {
        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].validity.valid;", driver.findElement(emailField));
    }

    public String getPasswordInputType() {
        return driver.findElement(passwordField).getAttribute("type");
    }

    public void loginAs(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
    }

    public String getAuthenticationError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(authenticationError)).getText();
    }

    public String getLoginError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginError)).getText();
    }

    public boolean isLoginErrorDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginError)).isDisplayed();
    }

    public boolean isAtLoginPage() {
        return LOGIN_PATH.equals(URI.create(driver.getCurrentUrl()).getPath());
    }
}
