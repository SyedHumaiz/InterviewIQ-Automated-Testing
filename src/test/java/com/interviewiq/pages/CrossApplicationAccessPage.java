package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

/** Navigation and redirect checks across the separate Company and Admin applications. */
public class CrossApplicationAccessPage {
    private static final Duration TIMEOUT = Duration.ofSeconds(12);
    private final WebDriver driver;
    private final WebDriverWait wait;

    public CrossApplicationAccessPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    public void openCompanyRoute(String route) {
        driver.get(resolve(ConfigManager.getCompanyBaseUrl(), route));
    }

    public void openAdminRoute(String route) {
        driver.get(resolve(ConfigManager.getAdminBaseUrl(), route));
    }

    public boolean isAtCompanyLogin() {
        return waitForLoginAt(ConfigManager.getCompanyBaseUrl());
    }

    public boolean isAtAdminLogin() {
        return waitForLoginAt(ConfigManager.getAdminBaseUrl());
    }

    private boolean waitForLoginAt(String applicationBaseUrl) {
        URI application = URI.create(applicationBaseUrl);
        return wait.until(webDriver -> {
            URI current = URI.create(webDriver.getCurrentUrl());
            return application.getHost().equalsIgnoreCase(current.getHost())
                    && "/login".equals(current.getPath());
        });
    }

    private static String resolve(String baseUrl, String route) {
        String relative = route.startsWith("/") ? route.substring(1) : route;
        return URI.create(baseUrl).resolve(relative).toString();
    }
}
