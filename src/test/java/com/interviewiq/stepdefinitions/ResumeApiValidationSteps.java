package com.interviewiq.stepdefinitions;

import com.interviewiq.api.CompanyApiSession;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResumeApiValidationSteps {
    private String candidateId;
    private Response response;

    @Given("an authenticated Company API session with an existing candidate")
    public void existingCandidate() {
        Response candidates = CompanyApiSession.authenticated().get("/api/company/candidates");
        assertEquals(200, candidates.statusCode(), "Could not retrieve candidates for resume validation");

        List<String> possiblePaths = List.of(
                "data.candidates[0]._id", "data.candidates[0].id",
                "candidates[0]._id", "candidates[0].id",
                "data[0]._id", "data[0].id",
                "[0]._id", "[0].id");
        for (String path : possiblePaths) {
            String value = candidates.jsonPath().getString(path);
            if (value != null && !value.isBlank()) {
                candidateId = value;
                return;
            }
        }
        throw new IllegalStateException("Resume validation requires at least one existing candidate");
    }

    @When("the Company API client submits a resume upload without a file")
    public void uploadWithoutFile() {
        response = CompanyApiSession.authenticated()
                .multiPart("note", "resume file intentionally omitted")
                .post(resumePath());
    }

    @When("the Company API client uploads non-PDF resume content")
    public void uploadNonPdf() {
        response = CompanyApiSession.authenticated()
                .multiPart("resume", "invalid-resume.txt",
                        "not a PDF".getBytes(StandardCharsets.UTF_8), "text/plain")
                .post(resumePath());
    }

    @Then("the resume upload request is rejected with status {int}")
    public void uploadRejected(int expectedStatus) {
        assertEquals(expectedStatus, response.statusCode(), "Unexpected resume upload response status");
    }

    private String resumePath() {
        return "/api/company/candidates/" + candidateId + "/resume";
    }
}
