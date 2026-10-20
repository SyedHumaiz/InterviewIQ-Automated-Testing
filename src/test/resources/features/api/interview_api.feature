Feature: Candidate interview-session security boundaries

  @api @security @interviews
  Scenario Outline: Candidate session operations require a redeemed candidate session
    When an anonymous API client sends "<method>" to "<path>"
    Then the workflow API response status is 401

    Examples:
      | method | path |
      | GET | /api/candidate/session |
      | POST | /api/candidate/session/start |
      | GET | /api/candidate/session/questions |
      | POST | /api/candidate/session/complete |
      | POST | /api/candidate/session/integrity |
      | POST | /api/candidate/session/questions/000000000000000000000001/answer |
      | POST | /api/candidate/session/questions/000000000000000000000001/skip |

  @api @security @interviews
  Scenario: Anonymous caller cannot cancel a Company interview
    When an anonymous API client sends "PATCH" to "/api/company/interviews/000000000000000000000001/cancel"
    Then the workflow API response status is 401

  @api @security @interviews @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario Outline: Unknown Company interview resources are hidden
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "<method>" to "<path>"
    Then the workflow API response status is 404

    Examples:
      | method | path |
      | PATCH | /api/company/interviews/000000000000000000000001/cancel |
      | GET | /api/company/interviews/000000000000000000000001/report |
