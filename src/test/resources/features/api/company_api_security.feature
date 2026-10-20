Feature: Company API authentication and access boundaries

  @api @security
  Scenario Outline: Anonymous clients cannot access Company workspace resources
    When an anonymous API client requests "<path>"
    Then the API request is rejected as unauthenticated

    Examples:
      | path |
      | /api/company-auth/me |
      | /api/company/current |
      | /api/company/candidates |
      | /api/company/positions |
      | /api/company/interviews |
      | /api/company/settings |
      | /api/company/audit-logs?page=1&limit=20 |
      | /api/company/dashboard/summary |
      | /api/company/dashboard/activity?days=7 |

  @api @security
  Scenario: Malformed bearer authentication cannot access candidates
    When an API client sends malformed bearer authentication to "/api/company/candidates"
    Then the API request is rejected as unauthenticated

  @api @security @authentication
  Scenario: Incorrect Company credentials are rejected
    When an API client submits invalid Company credentials
    Then the Company API login is rejected
