package com.interviewiq.stepdefinitions;

import com.interviewiq.config.ConfigManager;
import com.interviewiq.pages.AdminCompaniesPage;
import com.interviewiq.pages.AdminAuditPage;
import com.interviewiq.pages.AdminDashboardPage;
import com.interviewiq.pages.AdminLoginPage;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminSteps {
    private AdminLoginPage login; private AdminDashboardPage dashboard; private AdminCompaniesPage companies; private AdminAuditPage audit;
    @Given("the Platform Admin public application is available") public void publicApp() { login=new AdminLoginPage(DriverManager.getDriver()); }
    @When("the Platform Admin opens the login page") public void openLogin() { login.openLogin(); }
    @Then("the Platform Admin login page is displayed") public void loginDisplayed() { assertTrue(login.loginDisplayed()); }
    @Then("the Platform Admin login credentials are displayed") public void credentials() { assertTrue(login.credentialsDisplayed()); }
    @Then("the Platform Admin login fields use secure types") public void secureTypes() { assertEquals("email", login.emailType()); assertEquals("password", login.passwordType()); }
    @When("the Platform Admin submits login email {string} and password {string}") public void submitLogin(String email, String password) { login.submitLogin(email,password); }
    @Then("the Platform Admin login error is {string}") public void loginError(String text) { assertEquals(text,login.errorText()); }
    @When("the Platform Admin opens the signup page") public void openSignup() { login.openSignup(); }
    @Then("the Platform Admin signup page and fields are displayed") public void signupDisplayed() { assertTrue(login.signupDisplayed()); assertTrue(login.signupFieldsDisplayed()); }
    @When("the Platform Admin submits signup name {string}, email {string}, password {string}, and confirmation {string}") public void submitSignup(String name,String email,String password,String confirmation) { login.submitSignup(name,email,password,confirmation); }
    @Then("the Platform Admin signup error is {string}") public void signupError(String text) { assertEquals(text,login.errorText()); }
    @Given("the unauthenticated Platform Admin opens protected route {string}") public void protectedRoute(String route) { DriverManager.getDriver().get(ConfigManager.getAdminBaseUrl()+route.substring(1)); dashboard=new AdminDashboardPage(DriverManager.getDriver()); }
    @Then("the Platform Admin is redirected to login") public void redirected() { assertTrue(dashboard.atLogin()); }
    @Given("the Platform Admin signs in with valid configured credentials") public void validLogin() { login=new AdminLoginPage(DriverManager.getDriver()); login.openLogin(); login.loginAs(ConfigManager.getAdminTestEmail(),ConfigManager.getAdminTestPassword()); dashboard=new AdminDashboardPage(DriverManager.getDriver()); }
    @Then("the Platform Admin dashboard is displayed") public void dashboardDisplayed() { dashboard.waitForLoaded(); assertTrue(dashboard.displayed()); }
    @Then("the Platform Admin dashboard summary is displayed") public void dashboardSummary() { dashboard.waitForLoaded(); assertTrue(dashboard.summaryDisplayed()); }
    @Given("the shared authenticated Platform Admin session is ready") public void sharedSession() { dashboard=new AdminDashboardPage(DriverManager.getDriver()); dashboard.open(); }
    @Then("the Platform Admin primary navigation is displayed") public void navDisplayed() { assertTrue(dashboard.navigationDisplayed()); }
    @When("the Platform Admin opens Companies from navigation") public void navCompanies() { dashboard.openCompanies(); companies=new AdminCompaniesPage(DriverManager.getDriver()); }
    @Then("the Platform Admin Companies page is displayed") public void companiesDisplayed() { assertTrue(companies.displayed()); }
    @Then("the Platform Admin Companies filters and content state are displayed") public void companiesContent() { assertTrue(companies.filtersDisplayed(), companies.contentDiagnostics()); assertTrue(companies.listOrEmptyStateDisplayed(), companies.contentDiagnostics()); }
    @When("the Platform Admin searches for a nonexistent company") public void searchForMissingCompany() { companies.searchForNonexistentCompany(); }
    @Then("the Platform Admin Companies no-results state is displayed") public void noCompanyResults() { assertTrue(companies.noResultsDisplayed()); }
    @When("the Platform Admin selects the Active company status filter") public void selectActiveCompanies() { companies.selectActiveStatus(); }
    @Then("the Active company status filter remains selected") public void activeCompaniesSelected() { assertTrue(companies.activeStatusSelected()); }
    @When("the Platform Admin sorts Companies by company name") public void sortCompaniesByName() { companies.sortByCompanyName(); }
    @Then("the company-name sort remains selected") public void companyNameSortSelected() { assertTrue(companies.companyNameSortSelected()); }
    @When("the Platform Admin opens Audit from navigation") public void openAudit() { dashboard.openAudit(); audit=new AdminAuditPage(DriverManager.getDriver()); audit.waitForLoaded(); }
    @Then("the Platform Admin Audit content state is displayed") public void auditContent() { assertTrue(audit.hasContentState()); }
    @When("the Platform Admin opens the company registration form") public void openCompanyRegistration() { companies.openRegistration(); }
    @Then("the Platform Admin company registration form is displayed") public void registrationDisplayed() { assertTrue(companies.registrationDisplayed()); assertTrue(companies.registrationFieldsDisplayed()); }
    @When("the Platform Admin submits an empty company registration") public void blankRegistration() { companies.submitBlankRegistration(); }
    @Then("the Platform Admin company registration error is {string}") public void registrationError(String text) { assertEquals(text,companies.errorText()); }
}
