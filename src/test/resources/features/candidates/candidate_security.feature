@candidateSecurity
Feature: Candidate resource validation and filtering boundaries

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Malformed candidate detail resource is not disclosed
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "GET" to "/api/company/candidates/not-an-object-id"
    Then the workflow API response status is 404

  @api @security
  Scenario: Anonymous caller cannot archive a Company candidate
    When an anonymous API client sends "DELETE" to "/api/company/candidates/000000000000000000000001"
    Then the workflow API response status is 401

  @ui @candidates @requiresCredentials @requiresCompanyCredentials
  Scenario: Archived candidate is excluded from the Active filter
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user creates a unique automation candidate
    And the Company user returns to the candidate listing
    And the Company user archives the created candidate
    And the Company user filters candidates by "Active"
    Then the created candidate is not visible in the listing
