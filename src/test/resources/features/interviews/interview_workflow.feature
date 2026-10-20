@interviews
@interviewWorkflow
Feature: Company interview workflow and access boundaries

  @ui @companySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Authenticated Company user can access Interviews
    Given an authenticated Company user opens the Interviews page
    Then the Interviews page heading is displayed

  @ui @companySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Interviews page shows sessions or its legitimate empty state
    Given an authenticated Company user opens the Interviews page
    Then the interview session list or empty state is displayed

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Malformed interview report resource is not disclosed
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "GET" to "/api/company/interviews/not-an-object-id/report"
    Then the workflow API response status is 404

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Malformed interview cancellation resource is not disclosed
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "PATCH" to "/api/company/interviews/not-an-object-id/cancel"
    Then the workflow API response status is 404

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Malformed candidate interview relationship is not disclosed
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "GET" to "/api/company/candidates/not-an-object-id/interviews"
    Then the workflow API response status is 404

  @api @security
  Scenario: Anonymous caller cannot access a Company interview report
    When an anonymous API client sends "GET" to "/api/company/interviews/000000000000000000000001/report"
    Then the workflow API response status is 401
