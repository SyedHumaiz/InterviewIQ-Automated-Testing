package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

/** Navigates directly to Company workspace routes and verifies their access guard. */
public class CompanyProtectedRoutePage {

    private static final Duration REDIRECT_TIMEOUT = Duration.ofSeconds(10);

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CompanyProtectedRoutePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, REDIRECT_TIMEOUT);
    }

    public void open(String route) {
        driver.get(ConfigManager.getCompanyBaseUrl() + route);
    }

    public boolean isRedirectedToLogin() {
        return wait.until(webDriver -> "/login".equals(URI.create(webDriver.getCurrentUrl()).getPath()));
    }
}
