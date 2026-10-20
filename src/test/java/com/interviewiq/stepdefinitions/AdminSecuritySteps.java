package com.interviewiq.stepdefinitions;

import com.interviewiq.api.CompanyApiSession;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminSecuritySteps {
    private Response response;

    @Given("an authenticated Company API session for Admin isolation")
    public void companySession() {
        CompanyApiSession.authenticated();
    }

    @When("the Company API session requests Platform Admin identity")
    public void requestAdminIdentity() {
        response = CompanyApiSession.authenticated().get("/api/platform-admin/auth/me");
    }

    @Then("Platform Admin API access is rejected")
    public void adminAccessRejected() {
        assertTrue(response.statusCode() == 401 || response.statusCode() == 403,
                "Expected 401/403 but was " + response.statusCode());
    }
}
