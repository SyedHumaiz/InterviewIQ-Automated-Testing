package com.interviewiq.pages;

import com.interviewiq.config.ConfigManager;
import org.openqa.selenium.WebDriver;

/** Establishes and verifies an authenticated Company workspace in the scenario browser. */
public final class AuthenticatedCompanySession {
    private AuthenticatedCompanySession() {
    }

    public static void start(WebDriver driver) {
        CompanyLoginPage loginPage = new CompanyLoginPage(driver);
        loginPage.open();
        loginPage.loginAs(ConfigManager.getCompanyTestEmail(), ConfigManager.getCompanyTestPassword());
        new CompanyOverviewPage(driver).waitForLoaded();
    }
}
