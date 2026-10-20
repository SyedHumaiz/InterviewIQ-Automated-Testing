package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;
import java.util.stream.Collectors;

/** Public Platform Admin login and signup screens. */
public class AdminLoginPage {
    private static final Duration TIMEOUT = Duration.ofSeconds(12);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By loginForm = By.cssSelector("form.login-card");
    private final By signupHeading = By.xpath("//h2[normalize-space()='Create account']");
    private final By email = By.cssSelector("input[type='email']");
    private final By password = By.cssSelector("input[type='password']");
    private final By signIn = By.xpath("//button[normalize-space()='Sign in']");
    private final By createAccount = By.xpath("//button[normalize-space()='Create account']");
    private final By error = By.cssSelector(".inline-error");

    public AdminLoginPage(WebDriver driver) { this.driver = driver; this.wait = new WebDriverWait(driver, TIMEOUT); }
    public void openLogin() {
        driver.get(ConfigManager.getAdminBaseUrl() + "login");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginForm));
            wait.until(ExpectedConditions.visibilityOfElementLocated(email));
            wait.until(ExpectedConditions.elementToBeClickable(signIn));
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Admin login page did not render: " + diagnostics(), exception);
        }
    }
    public void openSignup() { driver.get(ConfigManager.getAdminBaseUrl() + "signup"); wait.until(ExpectedConditions.visibilityOfElementLocated(signupHeading)); }
    public boolean loginDisplayed() { return visible(loginForm) && visible(email) && visible(signIn); }
    public boolean signupDisplayed() { return driver.findElement(signupHeading).isDisplayed(); }
    public boolean credentialsDisplayed() { return driver.findElement(email).isDisplayed() && driver.findElement(password).isDisplayed(); }
    public String emailType() { return driver.findElement(email).getAttribute("type"); }
    public String passwordType() { return driver.findElement(password).getAttribute("type"); }
    public boolean signupFieldsDisplayed() { return driver.findElements(By.cssSelector("input")).size() == 4; }
    public void submitLogin(String address, String secret) { fill(email, address); fill(password, secret); driver.findElement(signIn).click(); }
    public void submitSignup(String name, String address, String secret, String confirmation) {
        fill(By.xpath("//label[normalize-space()='Name']//input"), name);
        fill(email, address);
        fill(By.xpath("//label[normalize-space()='Password']//input"), secret);
        fill(By.xpath("//label[normalize-space()='Confirm password']//input"), confirmation);
        driver.findElement(createAccount).click();
    }
    public String errorText() { return wait.until(ExpectedConditions.visibilityOfElementLocated(error)).getText(); }
    public boolean atLogin() { return "/login".equals(URI.create(driver.getCurrentUrl()).getPath()); }
    public void loginAs(String address, String secret) { submitLogin(address, secret); }
    private void fill(By locator, String value) { driver.findElement(locator).clear(); driver.findElement(locator).sendKeys(value); }
    private boolean visible(By locator) { return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed); }
    private String diagnostics() {
        String body = driver.findElements(By.tagName("body")).stream().filter(WebElement::isDisplayed)
                .map(WebElement::getText).collect(Collectors.joining(" ")).replaceAll("\\s+", " ").trim();
        return "url=" + driver.getCurrentUrl() + ", title=" + driver.getTitle()
                + ", body=" + body.substring(0, Math.min(body.length(), 300));
    }
}
