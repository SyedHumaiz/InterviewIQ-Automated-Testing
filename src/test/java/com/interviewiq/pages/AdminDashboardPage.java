package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URI;
import java.time.Duration;

public class AdminDashboardPage {
    private final WebDriver driver; private final WebDriverWait wait;
    private final By heading = By.xpath("//h1[normalize-space()='Platform overview']");
    public AdminDashboardPage(WebDriver driver) { this.driver=driver; this.wait=new WebDriverWait(driver, Duration.ofSeconds(12)); }
    public void open() { driver.get(ConfigManager.getAdminBaseUrl() + "app"); waitForLoaded(); }
    public void waitForLoaded() { wait.until(ExpectedConditions.visibilityOfElementLocated(heading)); }
    public boolean displayed() { return driver.findElement(heading).isDisplayed(); }
    public boolean summaryDisplayed() {
        return driver.findElement(By.cssSelector("section.stats")).isDisplayed()
                && driver.findElement(By.xpath("//h2[normalize-space()='Recent companies']")).isDisplayed()
                && driver.findElement(By.xpath("//h2[normalize-space()='Recent activity']")).isDisplayed();
    }
    public boolean navigationDisplayed() { return driver.findElement(By.cssSelector("a[href='/app/companies']")).isDisplayed() && driver.findElement(By.cssSelector("a[href='/app/admins']")).isDisplayed() && driver.findElement(By.cssSelector("a[href='/app/audit']")).isDisplayed() && driver.findElement(By.cssSelector("a[href='/app/settings']")).isDisplayed(); }
    public void openCompanies() { driver.findElement(By.cssSelector("a[href='/app/companies']")).click(); }
    public void openAudit() { driver.findElement(By.cssSelector("a[href='/app/audit']")).click(); }
    public void navigateAcrossProtectedPages() {
        openProtected("app/companies", "Companies");
        openProtected("app/audit", "Audit");
        openProtected("app/settings", "Settings");
    }
    public boolean remainsAuthenticated() {
        return "/app/settings".equals(URI.create(driver.getCurrentUrl()).getPath())
                && driver.findElement(By.cssSelector("button.sidebar-logout")).isDisplayed();
    }
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.sidebar-logout"))).click();
        wait.until(webDriver -> "/login".equals(URI.create(webDriver.getCurrentUrl()).getPath()));
    }
    private void openProtected(String relativePath, String expectedHeading) {
        driver.get(ConfigManager.getAdminBaseUrl() + relativePath);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[normalize-space()='" + expectedHeading + "']")));
    }
    public boolean atLogin() {
        try {
            wait.until(webDriver -> "/login".equals(URI.create(webDriver.getCurrentUrl()).getPath())
                    && webDriver.findElements(By.cssSelector("form.login-card input[type='email']"))
                    .stream().anyMatch(element -> element.isDisplayed()));
            return true;
        } catch (TimeoutException exception) { return false; }
    }
}
