@resumes
@resumeApiValidation
Feature: Resume API validation and ownership boundaries

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Malformed candidate resume cannot be uploaded
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "POST" to "/api/company/candidates/not-an-object-id/resume"
    Then the workflow API response status is 400

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Malformed candidate resume cannot be deleted
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "DELETE" to "/api/company/candidates/not-an-object-id/resume"
    Then the workflow API response status is 404

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Unknown candidate resume cannot be uploaded
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "POST" to "/api/company/candidates/000000000000000000000001/resume"
    Then the workflow API response status is 400

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Resume upload requires a multipart file
    Given an authenticated Company API session with an existing candidate
    When the Company API client submits a resume upload without a file
    Then the resume upload request is rejected with status 400

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Resume upload rejects non-PDF multipart content
    Given an authenticated Company API session with an existing candidate
    When the Company API client uploads non-PDF resume content
    Then the resume upload request is rejected with status 400
