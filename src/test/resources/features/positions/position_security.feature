@positionSecurity
Feature: Position mutation security boundaries

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Malformed Position resource cannot be updated
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "PATCH" to "/api/company/positions/not-an-object-id"
    Then the workflow API response status is 400

  @api @security
  Scenario: Anonymous caller cannot update a Company Position
    When an anonymous API client sends "PATCH" to "/api/company/positions/000000000000000000000001"
    Then the workflow API response status is 401
