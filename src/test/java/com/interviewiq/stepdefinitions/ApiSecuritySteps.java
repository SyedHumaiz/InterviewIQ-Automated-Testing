package com.interviewiq.stepdefinitions;

import com.interviewiq.api.ApiClient;
import com.interviewiq.api.CompanyApiSession;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiSecuritySteps {
    private Response response;
    @When("an anonymous API client requests {string}") public void anonymousGet(String path) { response=ApiClient.getAnonymous(path); }
    @When("an API client sends malformed bearer authentication to {string}") public void malformedBearer(String path) { response=ApiClient.anonymous().header("Authorization", "Bearer not-a-valid-token").get(path); }
    @Then("the API request is rejected as unauthenticated") public void rejected() { assertTrue(response.statusCode()==401 || response.statusCode()==403, "Expected 401/403 but was "+response.statusCode()); }
    @When("an API client submits invalid Company credentials") public void invalidLogin() { response=ApiClient.anonymous().contentType("application/json").body("{\"email\":\"api-invalid@interviewiq.invalid\",\"password\":\"InvalidPassword123!\"}").post("/api/company-auth/login"); }
    @Then("the Company API login is rejected") public void loginRejected() { assertTrue(response.statusCode()==401 || response.statusCode()==403, "Expected 401/403 but was "+response.statusCode()); }
    @Given("an authenticated Company API session") public void authenticatedSession() { CompanyApiSession.authenticated(); }
    @When("the authenticated Company API client requests {string}") public void authenticatedGet(String path) { response=CompanyApiSession.authenticated().get(path); }
    @Then("the API response is successful") public void successful() { assertEquals(200,response.statusCode(),"Unexpected API response"); }
    @When("the authenticated Company API client deletes unknown resource {string}") public void unknownDelete(String path) { response=CompanyApiSession.authenticated().delete(path); }
    @Then("the API response is not found") public void notFound() { assertEquals(404,response.statusCode(),"Unknown tenant-owned resource must not be exposed"); }
}
