Feature: Authenticated Company API resources

  @api @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario Outline: Authenticated Company session can read supported resources
    Given an authenticated Company API session
    When the authenticated Company API client requests "<path>"
    Then the API response is successful

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

  @api @security @candidates @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario Outline: Unknown candidate resources are not disclosed
    Given an authenticated Company API session
    When the authenticated Company API client deletes unknown resource "<path>"
    Then the API response is not found

    Examples:
      | path |
      | /api/company/candidates/000000000000000000000001 |
      | /api/company/candidates/000000000000000000000001/resume |

  @api @security @positions @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Unknown Position cannot be archived
    Given an authenticated Company API session
    When the authenticated Company API client deletes unknown resource "/api/company/positions/000000000000000000000001"
    Then the API response is not found
