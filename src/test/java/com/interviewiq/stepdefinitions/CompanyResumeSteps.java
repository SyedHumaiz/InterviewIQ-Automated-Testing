package com.interviewiq.stepdefinitions;

import com.interviewiq.pages.CompanyCandidateResumePage;
import com.interviewiq.pages.CompanyCandidatesPage;
import com.interviewiq.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyResumeSteps {
    private final List<Path> temporaryFiles = new ArrayList<>();
    private CompanyCandidateResumePage resumePage;

    @Given("an authenticated Company user opens a disposable candidate resume")
    public void openDisposableCandidateResume() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        CompanyCandidatesPage candidates = new CompanyCandidatesPage(DriverManager.getDriver());
        candidates.open();
        candidates.openAddCandidateForm();
        candidates.createCandidate(
                "Resume Automation " + suffix,
                "resume.automation." + suffix + "@example.com",
                fixturePath().toString());
        resumePage = new CompanyCandidateResumePage(DriverManager.getDriver());
        resumePage.openFromCreationConfirmation();
    }

    @When("the Company user removes and uploads a replacement PDF resume")
    public void removeAndUploadReplacementPdf() {
        assertTrue(resumePage.acceptsPdfFiles(), "Resume input must advertise PDF support");
        resumePage.remove();
        resumePage.uploadAndWaitForResume(temporaryPdf().toString());
    }

    @When("the Company user attempts to upload a non-PDF resume")
    public void uploadNonPdf() {
        resumePage.upload(temporaryTextFile().toString());
    }

    @When("the Company user attempts to upload an oversized PDF resume")
    public void uploadOversizedPdf() {
        resumePage.upload(temporaryOversizedPdf().toString());
    }

    @When("the Company user replaces the existing candidate resume")
    public void replaceExistingResume() {
        resumePage.remove();
        resumePage.uploadAndWaitForResume(temporaryPdf().toString());
    }

    @When("the Company user removes the candidate resume")
    public void removeResume() {
        resumePage.remove();
    }

    @When("the Company user refreshes the candidate profile")
    public void refreshCandidateProfile() {
        resumePage.refresh();
    }

    @Then("the candidate Resume area is displayed")
    public void resumeAreaDisplayed() {
        assertTrue(resumePage.isResumeAreaDisplayed());
    }

    @Then("the existing candidate resume state is displayed")
    public void existingResumeDisplayed() {
        assertTrue(resumePage.hasResume(), "Expected the candidate's existing resume state");
    }

    @Then("the replacement candidate resume is displayed")
    public void replacementResumeDisplayed() {
        assertTrue(resumePage.hasResume(), "Expected the replacement resume state");
    }

    @Then("the non-PDF resume validation message is displayed")
    public void nonPdfValidationDisplayed() {
        assertTrue(resumePage.isNonPdfErrorDisplayed());
    }

    @Then("the oversized resume validation message is displayed")
    public void oversizedValidationDisplayed() {
        assertTrue(resumePage.isOversizedErrorDisplayed());
    }

    @Then("the candidate resume empty state is displayed")
    public void resumeEmptyStateDisplayed() {
        assertTrue(resumePage.hasEmptyState());
    }

    @After("@resumes")
    public void deleteTemporaryFiles() {
        for (Path temporaryFile : temporaryFiles) {
            try {
                Files.deleteIfExists(temporaryFile);
            } catch (IOException exception) {
                throw new IllegalStateException("Could not remove temporary resume fixture", exception);
            }
        }
    }

    private Path fixturePath() {
        try {
            return Path.of(getClass().getClassLoader().getResource("fixtures/minimal-resume.pdf").toURI());
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("Could not resolve the PDF resume fixture", exception);
        }
    }

    private Path temporaryPdf() {
        try {
            Path path = Files.createTempFile("interviewiq-resume-replacement-", ".pdf");
            Files.copy(fixturePath(), path, StandardCopyOption.REPLACE_EXISTING);
            temporaryFiles.add(path);
            return path;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create a temporary PDF resume", exception);
        }
    }

    private Path temporaryTextFile() {
        try {
            Path path = Files.createTempFile("interviewiq-resume-invalid-", ".txt");
            Files.writeString(path, "This is intentionally not a PDF.");
            temporaryFiles.add(path);
            return path;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create a temporary non-PDF resume", exception);
        }
    }

    private Path temporaryOversizedPdf() {
        try {
            Path path = Files.createTempFile("interviewiq-resume-oversized-", ".pdf");
            try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw")) {
                file.setLength(5L * 1024 * 1024 + 1);
            }
            temporaryFiles.add(path);
            return path;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create a temporary oversized PDF", exception);
        }
    }
}
