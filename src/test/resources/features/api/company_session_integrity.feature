@api
@security
@companySessionIntegrity
Feature: Company API session integrity variants

  Scenario: Empty Company session cookie does not authenticate identity
    When an API client requests "/api/company-auth/me" with an empty Company session cookie
    Then the invalid Company API session is rejected

  Scenario: Empty bearer credential does not authenticate identity
    When an API client requests "/api/company-auth/me" with an empty bearer credential
    Then the invalid Company API session is rejected

  Scenario: Invalid Company session cannot expose current Company data
    When an API client requests "/api/company/current" with an invalid Company session cookie
    Then the invalid Company API session is rejected

  Scenario: Invalid Company session cannot expose Company settings
    When an API client requests "/api/company/settings" with an invalid Company session cookie
    Then the invalid Company API session is rejected
