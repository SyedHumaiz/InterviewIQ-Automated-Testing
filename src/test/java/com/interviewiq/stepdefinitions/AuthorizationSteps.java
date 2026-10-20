package com.interviewiq.stepdefinitions;

import com.interviewiq.pages.AuthenticatedAdminSession;
import com.interviewiq.pages.AuthenticatedCompanySession;
import com.interviewiq.pages.CrossApplicationAccessPage;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthorizationSteps {
    private CrossApplicationAccessPage accessPage;

    @When("an anonymous user opens protected Company route {string}")
    public void anonymousCompanyRoute(String route) {
        accessPage = new CrossApplicationAccessPage(DriverManager.getDriver());
        accessPage.openCompanyRoute(route);
    }

    @When("an anonymous user opens protected Admin route {string}")
    public void anonymousAdminRoute(String route) {
        accessPage = new CrossApplicationAccessPage(DriverManager.getDriver());
        accessPage.openAdminRoute(route);
    }

    @Given("a Company user is authenticated for an isolated authorization check")
    public void isolatedCompanyAuthentication() {
        AuthenticatedCompanySession.start(DriverManager.getDriver());
        accessPage = new CrossApplicationAccessPage(DriverManager.getDriver());
    }

    @When("the Company user opens the Admin dashboard route")
    public void companyUserOpensAdmin() {
        accessPage.openAdminRoute("/app");
    }

    @Given("a Platform Admin is authenticated for an isolated authorization check")
    public void isolatedAdminAuthentication() {
        AuthenticatedAdminSession.start(DriverManager.getDriver());
        accessPage = new CrossApplicationAccessPage(DriverManager.getDriver());
    }

    @When("the Platform Admin opens the Company workspace route")
    public void adminOpensCompany() {
        accessPage.openCompanyRoute("/app");
    }

    @Then("the Company application redirects to its login page")
    public void companyLoginRedirect() {
        assertTrue(accessPage.isAtCompanyLogin());
    }

    @Then("the Admin application redirects to its login page")
    public void adminLoginRedirect() {
        assertTrue(accessPage.isAtAdminLogin());
    }
}
