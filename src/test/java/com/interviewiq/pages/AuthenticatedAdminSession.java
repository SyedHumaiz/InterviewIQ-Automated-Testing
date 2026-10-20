package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.WebDriver;

/** Starts the one shared, credentialed Platform Admin browser session. */
public final class AuthenticatedAdminSession {
    private AuthenticatedAdminSession() { }
    public static void start(WebDriver driver) {
        AdminLoginPage login = new AdminLoginPage(driver);
        login.openLogin();
        login.loginAs(ConfigManager.getAdminTestEmail(), ConfigManager.getAdminTestPassword());
        new AdminDashboardPage(driver).waitForLoaded();
    }
}
