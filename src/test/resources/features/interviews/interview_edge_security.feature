@interviews
@interviewEdgeSecurity
Feature: Interview endpoint validation boundaries

  @api @security
  Scenario: Anonymous caller cannot create a Company interview
    When an anonymous API client sends "POST" to "/api/company/interviews"
    Then the workflow API response status is 401

  @api @security
  Scenario: Anonymous caller cannot access candidate interview relationships
    When an anonymous API client sends "GET" to "/api/company/candidates/000000000000000000000001/interviews"
    Then the workflow API response status is 401

  @api @security
  Scenario: Anonymous caller cannot inspect a malformed interview report
    When an anonymous API client sends "GET" to "/api/company/interviews/not-an-object-id/report"
    Then the workflow API response status is 401

  @api @security
  Scenario: Anonymous caller cannot cancel a malformed interview resource
    When an anonymous API client sends "PATCH" to "/api/company/interviews/not-an-object-id/cancel"
    Then the workflow API response status is 401
