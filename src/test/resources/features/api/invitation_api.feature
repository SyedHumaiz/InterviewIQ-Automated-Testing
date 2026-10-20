Feature: Invitation API access and unknown-state handling

  @api @security @invitations
  Scenario Outline: Unknown public invitation tokens cannot be used
    When an anonymous API client sends "<method>" to "<path>"
    Then the workflow API response status is 404

    Examples:
      | method | path |
      | GET | /api/candidate/invitations/not-a-real-automation-token/preview |
      | POST | /api/candidate/invitations/not-a-real-automation-token/redeem |

  @api @security @invitations
  Scenario Outline: Anonymous callers cannot send or revoke Company invitations
    When an anonymous API client sends "<method>" to "<path>"
    Then the workflow API response status is 401

    Examples:
      | method | path |
      | POST | /api/company/interviews/000000000000000000000001/invitation |
      | POST | /api/company/invitations/000000000000000000000001/revoke |

  @api @security @invitations @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario Outline: Unknown Company invitation resources are not changed
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "<method>" to "<path>"
    Then the workflow API response status is 404

    Examples:
      | method | path |
      | POST | /api/company/interviews/000000000000000000000001/invitation |
      | POST | /api/company/invitations/000000000000000000000001/revoke |
