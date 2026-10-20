@adminSecurity
Feature: Platform Admin API and route isolation

  @api @security
  Scenario: Anonymous request cannot access Platform Admin identity
    When an anonymous API client sends "GET" to "/api/platform-admin/auth/me"
    Then the workflow API response status is 401

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Company API session cannot authenticate as Platform Admin
    Given an authenticated Company API session for Admin isolation
    When the Company API session requests Platform Admin identity
    Then Platform Admin API access is rejected

  @ui @security
  Scenario: Anonymous browser cannot access an Admin company detail route
    When an anonymous user opens protected Admin route "/app/companies/000000000000000000000001"
    Then the Admin application redirects to its login page
