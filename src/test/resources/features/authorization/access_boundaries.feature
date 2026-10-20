@authorization
@authorizationWorkflow
Feature: Cross-application authorization boundaries

  @ui @security
  Scenario: Anonymous user cannot access a Company candidate detail route
    When an anonymous user opens protected Company route "/app/candidates/000000000000000000000001"
    Then the Company application redirects to its login page

  @ui @security
  Scenario: Anonymous user cannot access the Admin company registration route
    When an anonymous user opens protected Admin route "/app/companies/new"
    Then the Admin application redirects to its login page

  @ui @security @requiresCredentials @requiresCompanyCredentials
  Scenario: Company authentication does not grant Admin dashboard access
    Given a Company user is authenticated for an isolated authorization check
    When the Company user opens the Admin dashboard route
    Then the Admin application redirects to its login page

  @ui @security @requiresCredentials @requiresAdminCredentials
  Scenario: Admin authentication does not establish a Company workspace session
    Given a Platform Admin is authenticated for an isolated authorization check
    When the Platform Admin opens the Company workspace route
    Then the Company application redirects to its login page

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Unknown candidate detail does not disclose a protected entity
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "GET" to "/api/company/candidates/000000000000000000000001"
    Then the workflow API response status is 404
