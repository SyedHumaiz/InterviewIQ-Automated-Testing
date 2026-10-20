Feature: InterviewIQ environment configuration

  Scenario: InterviewIQ application URLs are configured
    Given the InterviewIQ environment configuration is loaded
    Then the candidate application base URL should be configured
    And the company application base URL should be configured
    And the admin application base URL should be configured
