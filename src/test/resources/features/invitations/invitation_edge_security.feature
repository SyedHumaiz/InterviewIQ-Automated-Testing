@invitations
@invitationEdgeSecurity
Feature: Invitation endpoint edge security

  @api @security
  Scenario: Invitation preview route rejects a missing token
    When an anonymous API client sends "GET" to "/api/candidate/invitations/preview"
    Then the workflow API response status is 404

  @api @security
  Scenario: Invitation redemption route rejects a missing token
    When an anonymous API client sends "POST" to "/api/candidate/invitations/redeem"
    Then the workflow API response status is 404

  @api @security
  Scenario: Invitation preview rejects a token containing unsupported punctuation
    When an anonymous API client sends "GET" to "/api/candidate/invitations/invalid.token/preview"
    Then the workflow API response status is 404

  @api @security
  Scenario: Anonymous caller cannot revoke a malformed Company invitation resource
    When an anonymous API client sends "POST" to "/api/company/invitations/not-an-object-id/revoke"
    Then the workflow API response status is 401
