Feature: Resume API ownership and upload boundaries

  @api @security @resumes
  Scenario Outline: Anonymous clients cannot access resume operations
    When an anonymous API client sends "<method>" to "<path>"
    Then the workflow API response status is 401

    Examples:
      | method | path |
      | GET | /api/company/candidates/000000000000000000000001/resume |
      | POST | /api/company/candidates/000000000000000000000001/resume |
      | DELETE | /api/company/candidates/000000000000000000000001/resume |

  @api @security @resumes @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Malformed candidate resume resource is not disclosed
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "GET" to "/api/company/candidates/not-an-object-id/resume"
    Then the workflow API response status is 404

  @api @security @resumes @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario Outline: Unknown candidate resume resources are hidden
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "<method>" to "<path>"
    Then the workflow API response status is 404

    Examples:
      | method | path |
      | GET | /api/company/candidates/000000000000000000000001/resume |
      | GET | /api/company/candidates/000000000000000000000001/interviews |
