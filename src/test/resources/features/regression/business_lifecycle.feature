@regression
Feature: Company hiring lifecycle regression

  @ui @candidates @requiresCredentials @requiresCompanyCredentials
  Scenario: Company user completes a candidate lifecycle
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user creates a unique automation candidate
    And the Company user returns to the candidate listing
    And the Company user searches for the created candidate
    Then the created candidate is visible in the listing
    When the Company user filters candidates by "All candidates"
    And the Company user archives the created candidate
    And the Company user filters candidates by "Archived"
    Then the created candidate is shown as archived
    And the created candidate is visible in the listing

  @ui @positions @requiresCredentials @requiresCompanyCredentials
  Scenario: Company user completes a Position lifecycle
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user creates a unique automation Position
    Then the created Position is visible
    And the created Position is Active
    When the Company user opens the created Position for editing
    And the Company user updates the created Position title
    Then the updated Position title is visible
    When the Company user archives the created Position
    Then the created Position is Archived
