@adminManagement
Feature: Platform Admin management content

  @ui @admin @adminSession @requiresCredentials @requiresAdminCredentials
  Scenario: Admin dashboard displays platform summary content
    Given the shared authenticated Platform Admin session is ready
    Then the Platform Admin dashboard summary is displayed

  @ui @admin @adminSession @requiresCredentials @requiresAdminCredentials
  Scenario: Admin Companies search reports no matches safely
    Given the shared authenticated Platform Admin session is ready
    When the Platform Admin opens Companies from navigation
    And the Platform Admin searches for a nonexistent company
    Then the Platform Admin Companies no-results state is displayed

  @ui @admin @adminSession @requiresCredentials @requiresAdminCredentials
  Scenario: Admin Companies status filter can be changed
    Given the shared authenticated Platform Admin session is ready
    When the Platform Admin opens Companies from navigation
    And the Platform Admin selects the Active company status filter
    Then the Active company status filter remains selected

  @ui @admin @adminSession @requiresCredentials @requiresAdminCredentials
  Scenario: Admin Companies sorting control can order by company name
    Given the shared authenticated Platform Admin session is ready
    When the Platform Admin opens Companies from navigation
    And the Platform Admin sorts Companies by company name
    Then the company-name sort remains selected

  @ui @admin @adminSession @requiresCredentials @requiresAdminCredentials
  Scenario: Admin Audit page displays records or its legitimate empty state
    Given the shared authenticated Platform Admin session is ready
    When the Platform Admin opens Audit from navigation
    Then the Platform Admin Audit content state is displayed
