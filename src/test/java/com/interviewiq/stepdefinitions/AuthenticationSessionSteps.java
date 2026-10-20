package com.interviewiq.stepdefinitions;

import com.interviewiq.api.ApiClient;
import com.interviewiq.pages.AdminDashboardPage;
import com.interviewiq.pages.AuthenticatedAdminSession;
import com.interviewiq.pages.AuthenticatedCompanySession;
import com.interviewiq.pages.CompanySessionPage;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthenticationSessionSteps {
    private CompanySessionPage companySessionPage;
    private AdminDashboardPage adminDashboardPage;
    private Response response;

    @Given("the shared authenticated Company browser session is ready")
    public void sharedCompanySessionReady() {
        companySessionPage = new CompanySessionPage(DriverManager.getDriver());
    }

    @When("the Company user navigates between Candidates, Positions, and Settings")
    public void navigateCompanyWorkspace() {
        companySessionPage.navigateAcrossProtectedPages();
    }

    @Then("the Company session remains in the authenticated workspace")
    public void companySessionRemainsAuthenticated() {
        assertTrue(companySessionPage.remainsAuthenticated());
    }

    @When("the Platform Admin navigates between Companies, Audit, and Settings")
    public void navigateAdminWorkspace() {
        adminDashboardPage = new AdminDashboardPage(DriverManager.getDriver());
        adminDashboardPage.navigateAcrossProtectedPages();
    }

    @Then("the Platform Admin session remains authenticated")
    public void adminSessionRemainsAuthenticated() {
        assertTrue(adminDashboardPage.remainsAuthenticated());
    }

    @Given("a Company user is authenticated for an isolated session check")
    public void isolatedCompanySession() {
        AuthenticatedCompanySession.start(DriverManager.getDriver());
        companySessionPage = new CompanySessionPage(DriverManager.getDriver());
    }

    @When("the Company user logs out")
    public void companyLogout() {
        companySessionPage.logout();
    }

    @Then("the Company login page is restored")
    public void companyLoginRestored() {
        assertTrue(companySessionPage.isAtLogin());
    }

    @Given("a Platform Admin is authenticated for an isolated session check")
    public void isolatedAdminSession() {
        AuthenticatedAdminSession.start(DriverManager.getDriver());
        adminDashboardPage = new AdminDashboardPage(DriverManager.getDriver());
    }

    @When("the Platform Admin logs out")
    public void adminLogout() {
        adminDashboardPage.logout();
    }

    @Then("the Admin login page is restored")
    public void adminLoginRestored() {
        assertTrue(adminDashboardPage.atLogin());
    }

    @When("an API client calls Company identity with an invalid session cookie")
    public void invalidSessionCookie() {
        response = ApiClient.anonymous().cookie("token", "not-a-valid-session-token")
                .get("/api/company-auth/me");
    }

    @When("an API client calls Company identity with an invalid bearer token")
    public void invalidBearerToken() {
        response = ApiClient.anonymous().header("Authorization", "Bearer not-a-valid-session-token")
                .get("/api/company-auth/me");
    }

    @When("an API client requests {string} with an empty Company session cookie")
    public void emptySessionCookie(String path) {
        response = ApiClient.anonymous().header("Cookie", "token=").get(path);
    }

    @When("an API client requests {string} with an empty bearer credential")
    public void emptyBearerCredential(String path) {
        response = ApiClient.anonymous().header("Authorization", "Bearer").get(path);
    }

    @When("an API client requests {string} with an invalid Company session cookie")
    public void invalidSessionCookie(String path) {
        response = ApiClient.anonymous().cookie("token", "not-a-valid-session-token").get(path);
    }

    @Then("the invalid Company API session is rejected")
    public void invalidSessionRejected() {
        assertTrue(response.statusCode() == 401 || response.statusCode() == 403,
                "Expected 401/403 but was " + response.statusCode());
    }
}
