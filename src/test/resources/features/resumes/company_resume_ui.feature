@ui
@resumes
@resumeWorkflow
@companySession
@requiresCredentials
@requiresCompanyCredentials
Feature: Company candidate resume management

  Scenario: Authenticated Company user can reach a candidate resume area
    Given an authenticated Company user opens a disposable candidate resume
    Then the candidate Resume area is displayed

  Scenario: Candidate resume information is displayed when a resume exists
    Given an authenticated Company user opens a disposable candidate resume
    Then the existing candidate resume state is displayed

  Scenario: Candidate resume upload accepts PDF files
    Given an authenticated Company user opens a disposable candidate resume
    When the Company user removes and uploads a replacement PDF resume
    Then the replacement candidate resume is displayed

  Scenario: Candidate resume upload rejects a non-PDF file
    Given an authenticated Company user opens a disposable candidate resume
    When the Company user attempts to upload a non-PDF resume
    Then the non-PDF resume validation message is displayed

  Scenario: Candidate resume upload rejects a file larger than five megabytes
    Given an authenticated Company user opens a disposable candidate resume
    When the Company user attempts to upload an oversized PDF resume
    Then the oversized resume validation message is displayed

  Scenario: Company user can replace an existing candidate resume
    Given an authenticated Company user opens a disposable candidate resume
    When the Company user replaces the existing candidate resume
    Then the replacement candidate resume is displayed

  Scenario: Company user can remove an existing candidate resume
    Given an authenticated Company user opens a disposable candidate resume
    When the Company user removes the candidate resume
    Then the candidate resume empty state is displayed

  Scenario: Candidate resume state remains consistent after refresh
    Given an authenticated Company user opens a disposable candidate resume
    When the Company user refreshes the candidate profile
    Then the existing candidate resume state is displayed
