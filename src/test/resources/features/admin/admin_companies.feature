Feature: Platform Admin company management

  @ui @admin @adminSession @requiresCredentials @requiresAdminCredentials
  Scenario: Authenticated Platform Admin dashboard shows navigation
    Given the shared authenticated Platform Admin session is ready
    Then the Platform Admin dashboard is displayed
    And the Platform Admin primary navigation is displayed

  @ui @admin @adminSession @requiresCredentials @requiresAdminCredentials
  Scenario: Authenticated Platform Admin opens Companies from navigation
    Given the shared authenticated Platform Admin session is ready
    When the Platform Admin opens Companies from navigation
    Then the Platform Admin Companies page is displayed

  @ui @admin @adminSession @requiresCredentials @requiresAdminCredentials
  Scenario: Companies page provides filters and a content state
    Given the shared authenticated Platform Admin session is ready
    When the Platform Admin opens Companies from navigation
    Then the Platform Admin Companies filters and content state are displayed

  @ui @admin @adminSession @requiresCredentials @requiresAdminCredentials
  Scenario: Companies page opens company registration
    Given the shared authenticated Platform Admin session is ready
    When the Platform Admin opens Companies from navigation
    And the Platform Admin opens the company registration form
    Then the Platform Admin company registration form is displayed

  @ui @admin @adminSession @requiresCredentials @requiresAdminCredentials @negative
  Scenario: Company registration requires every required field
    Given the shared authenticated Platform Admin session is ready
    When the Platform Admin opens Companies from navigation
    And the Platform Admin opens the company registration form
    And the Platform Admin submits an empty company registration
    Then the Platform Admin company registration error is "Complete every required field."
