package com.interviewiq.stepdefinitions;

import com.interviewiq.api.ApiClient;
import com.interviewiq.api.CompanyApiSession;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Safe negative-path coverage for resume, invitation, and candidate-session contracts. */
public class ResumeInvitationInterviewApiSteps {
    private Response response;
    @When("an anonymous API client sends {string} to {string}")
    public void anonymousRequest(String method, String path) { response = request(ApiClient.anonymous(), method, path); }
    @Given("an authenticated Company API session for workflow API tests")
    public void session() { CompanyApiSession.authenticated(); }
    @When("the authenticated Company API client sends {string} to {string}")
    public void authenticatedRequest(String method, String path) { response = request(CompanyApiSession.authenticated(), method, path); }
    @Then("the workflow API response status is {int}")
    public void status(int expected) { assertEquals(expected, response.statusCode(), "Unexpected workflow API response status"); }
    private Response request(RequestSpecification request, String method, String path) {
        return switch (method) {
            case "GET" -> request.get(path);
            case "POST" -> request.post(path);
            case "DELETE" -> request.delete(path);
            case "PATCH" -> request.patch(path);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };
    }
}
