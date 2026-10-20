@settingsWorkflow
Feature: Company workspace settings

  @ui @companySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Authenticated Company user can open Settings
    Given an authenticated Company user opens Settings
    Then the Company Settings heading is displayed

  @ui @companySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Settings displays Company profile and interview defaults
    Given an authenticated Company user opens Settings
    Then the Company settings sections are displayed

  @ui @companySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Default invitation expiry enforces the supported range
    Given an authenticated Company user opens Settings
    When the Company user enters an invitation expiry below the supported range
    Then the invitation expiry fails browser validation

  @api @security
  Scenario: Anonymous caller cannot mutate Company settings
    When an anonymous API client sends "PATCH" to "/api/company/settings"
    Then the workflow API response status is 401
