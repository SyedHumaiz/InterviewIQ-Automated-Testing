package com.interviewiq.stepdefinitions;

import com.interviewiq.config.ConfigManager;
import com.interviewiq.pages.CompanyLoginPage;
import com.interviewiq.pages.CompanyOverviewPage;
import com.interviewiq.pages.CompanyProtectedRoutePage;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyLoginSteps {

    private static final String INVALID_EMAIL = "automation.invalid@interviewiq.invalid";
    private static final String INVALID_PASSWORD = "InvalidPassword123!";

    private CompanyLoginPage companyLoginPage;
    private CompanyOverviewPage companyOverviewPage;
    private CompanyProtectedRoutePage companyProtectedRoutePage;

    @Given("the Company application is available")
    public void theCompanyApplicationIsAvailable() {
        companyLoginPage = new CompanyLoginPage(DriverManager.getDriver());
    }

    @When("the Company user opens the login page")
    public void theCompanyUserOpensTheLoginPage() {
        companyLoginPage.open();
    }

    @Then("the Company login page should be displayed")
    public void theCompanyLoginPageShouldBeDisplayed() {
        assertTrue(companyLoginPage.isLoginPageDisplayed());
    }

    @Then("the Company login email and password fields should be displayed")
    public void theCompanyLoginEmailAndPasswordFieldsShouldBeDisplayed() {
        assertTrue(companyLoginPage.areCredentialFieldsDisplayed());
    }

    @Then("the Company login sign in button should be displayed")
    public void theCompanyLoginSignInButtonShouldBeDisplayed() {
        assertTrue(companyLoginPage.isSignInButtonDisplayed());
    }

    @Given("the Company user is on the login page")
    public void theCompanyUserIsOnTheLoginPage() {
        companyLoginPage = new CompanyLoginPage(DriverManager.getDriver());
        companyLoginPage.open();
    }

    @When("the Company user signs in with invalid credentials")
    public void theCompanyUserSignsInWithInvalidCredentials() {
        companyLoginPage.enterEmail(INVALID_EMAIL);
        companyLoginPage.enterPassword(INVALID_PASSWORD);
        companyLoginPage.clickSignIn();
    }

    @When("the Company user submits email {string} and password {string}")
    public void theCompanyUserSubmitsEmailAndPassword(String email, String password) {
        companyLoginPage.clearCredentials();
        companyLoginPage.enterEmail(email);
        companyLoginPage.enterPassword(password);
        companyLoginPage.submit();
    }

    @Then("the Company login error should be {string}")
    public void theCompanyLoginErrorShouldBe(String expectedError) {
        assertEquals(expectedError, companyLoginPage.getLoginError());
    }

    @Then("the Company login should display a rejection error")
    public void theCompanyLoginShouldDisplayARejectionError() {
        assertTrue(companyLoginPage.isLoginErrorDisplayed());
    }

    @Then("the Company email value should fail browser validation")
    public void theCompanyEmailValueShouldFailBrowserValidation() {
        assertTrue(!companyLoginPage.isEmailValueValid());
    }

    @Then("the Company credential fields should use secure input types")
    public void theCompanyCredentialFieldsShouldUseSecureInputTypes() {
        assertEquals("email", companyLoginPage.getEmailInputType());
        assertEquals("password", companyLoginPage.getPasswordInputType());
    }

    @Then("the Company login should be rejected")
    public void theCompanyLoginShouldBeRejected() {
        assertTrue(companyLoginPage.isLoginErrorDisplayed());
    }

    @Then("the Company user should remain on the login page")
    public void theCompanyUserShouldRemainOnTheLoginPage() {
        assertTrue(companyLoginPage.isAtLoginPage());
    }

    @When("the Company user signs in with valid credentials")
    public void theCompanyUserSignsInWithValidCredentials() {
        companyLoginPage.loginAs(
                ConfigManager.getCompanyTestEmail(),
                ConfigManager.getCompanyTestPassword());
        companyOverviewPage = new CompanyOverviewPage(DriverManager.getDriver());
    }

    @Then("the Company user should enter the authenticated workspace")
    public void theCompanyUserShouldEnterTheAuthenticatedWorkspace() {
        companyOverviewPage.waitForLoaded();
        assertTrue(companyOverviewPage.isAtOverviewPage());
    }

    @Then("the Company user should no longer be on the login page")
    public void theCompanyUserShouldNoLongerBeOnTheLoginPage() {
        assertTrue(!companyLoginPage.isAtLoginPage());
    }

    @Then("the Company Overview page should be displayed")
    public void theCompanyOverviewPageShouldBeDisplayed() {
        assertTrue(companyOverviewPage.isOverviewHeadingDisplayed());
    }

    @Given("the unauthenticated Company user opens protected route {string}")
    public void theUnauthenticatedCompanyUserOpensProtectedRoute(String route) {
        companyProtectedRoutePage = new CompanyProtectedRoutePage(DriverManager.getDriver());
        companyProtectedRoutePage.open(route);
    }

    @Then("the Company user should be redirected to the login page")
    public void theCompanyUserShouldBeRedirectedToTheLoginPage() {
        assertTrue(companyProtectedRoutePage.isRedirectedToLogin());
    }
}
