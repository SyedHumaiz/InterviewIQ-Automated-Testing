@sessionManagement
Feature: Company and Admin session lifecycle

  @ui @companySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Company session remains authenticated across protected workspace navigation
    Given the shared authenticated Company browser session is ready
    When the Company user navigates between Candidates, Positions, and Settings
    Then the Company session remains in the authenticated workspace

  @ui @adminSession @requiresCredentials @requiresAdminCredentials
  Scenario: Admin session remains authenticated across protected management navigation
    Given the shared authenticated Platform Admin session is ready
    When the Platform Admin navigates between Companies, Audit, and Settings
    Then the Platform Admin session remains authenticated

  @ui @requiresCredentials @requiresCompanyCredentials
  Scenario: Company logout returns the user to login
    Given a Company user is authenticated for an isolated session check
    When the Company user logs out
    Then the Company login page is restored

  @ui @requiresCredentials @requiresAdminCredentials
  Scenario: Admin logout returns the user to Admin login
    Given a Platform Admin is authenticated for an isolated session check
    When the Platform Admin logs out
    Then the Admin login page is restored

  @api @security
  Scenario: Invalid Company session cookie does not authenticate
    When an API client calls Company identity with an invalid session cookie
    Then the invalid Company API session is rejected

  @api @security
  Scenario: Invalid bearer token does not authenticate Company identity
    When an API client calls Company identity with an invalid bearer token
    Then the invalid Company API session is rejected
