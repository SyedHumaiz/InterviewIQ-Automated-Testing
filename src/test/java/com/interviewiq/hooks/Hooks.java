package com.interviewiq.hooks;

import com.interviewiq.pages.AuthenticatedCompanySession;
import com.interviewiq.pages.AuthenticatedAdminSession;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {
    private static boolean credentialedCompanySessionStarted;
    private static boolean credentialedAdminSessionStarted;

    @Before("@ui")
    public void startBrowser(Scenario scenario) {
        if (usesSharedCompanySession(scenario)) {
            startSharedCompanySession();
        } else if (usesSharedAdminSession(scenario)) {
            startSharedAdminSession();
        } else {
            resetSharedSessions();
            DriverManager.initializeChromeDriver();
        }
    }

    @After("@ui")
    public void stopBrowser(Scenario scenario) {
        if (!usesSharedCompanySession(scenario) && !usesSharedAdminSession(scenario)) {
            DriverManager.quitDriver();
        }
    }

    @AfterAll
    public static void stopSharedCompanySession() {
        resetSharedSessions();
    }

    private static synchronized void startSharedCompanySession() {
        if (!credentialedCompanySessionStarted) {
            DriverManager.initializeChromeDriver();
            AuthenticatedCompanySession.start(DriverManager.getDriver());
            credentialedCompanySessionStarted = true;
        }
    }

    private static synchronized void startSharedAdminSession() {
        if (!credentialedAdminSessionStarted) {
            DriverManager.initializeChromeDriver();
            AuthenticatedAdminSession.start(DriverManager.getDriver());
            credentialedAdminSessionStarted = true;
        }
    }

    private static synchronized void resetSharedSessions() {
        if (credentialedCompanySessionStarted || credentialedAdminSessionStarted) {
            DriverManager.quitDriver();
        }
        credentialedCompanySessionStarted = false;
        credentialedAdminSessionStarted = false;
    }

    private static boolean usesSharedCompanySession(Scenario scenario) {
        return scenario.getSourceTagNames().contains("@companySession")
                || scenario.getSourceTagNames().contains("@candidates")
                || scenario.getSourceTagNames().contains("@positions");
    }

    private static boolean usesSharedAdminSession(Scenario scenario) {
        return scenario.getSourceTagNames().contains("@adminSession");
    }
}
