@invitations
@invitationWorkflow
Feature: Invitation workflow and disclosure boundaries

  @ui @companySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Company user can access invitation actions from Interviews
    Given an authenticated Company user opens the Interviews page
    Then the invitation workflow controls are available

  @ui @companySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Company user can assign an interview and send its invitation
    Given an authenticated Company user opens the Interviews page
    When the Company user assigns an eligible candidate to an active position
    And the Company user sends the new interview invitation
    Then invitation generation is confirmed

  @ui @security
  Scenario: Invalid public invitation token is rejected without candidate details
    When an anonymous candidate opens an invalid invitation token
    Then the public invitation unavailable state is displayed

  @api @security
  Scenario: Unknown opaque invitation token is not disclosed
    When an anonymous API client sends "GET" to "/api/candidate/invitations/000000000000000000000001/preview"
    Then the workflow API response status is 404

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Malformed invitation resource cannot be revoked
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "POST" to "/api/company/invitations/not-an-object-id/revoke"
    Then the workflow API response status is 400

  @api @security @apiCompanySession @requiresCredentials @requiresCompanyCredentials
  Scenario: Invitation action rejects a malformed Company interview resource
    Given an authenticated Company API session for workflow API tests
    When the authenticated Company API client sends "POST" to "/api/company/interviews/not-an-object-id/invitation"
    Then the workflow API response status is 400
