@ui
@positions
@requiresCredentials
Feature: Company Position Management

  Scenario: Authenticated Company user can open Positions
    Given an authenticated Company user opens the Positions page
    Then the Positions page heading is displayed

  Scenario: Positions page provides a New position control
    Given an authenticated Company user opens the Positions page
    Then the New position control is available

  Scenario: Positions page displays cards or an empty state
    Given an authenticated Company user opens the Positions page
    Then Position cards or the legitimate empty state are displayed

  Scenario: Company user can open the New position form
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    Then the New position form is displayed

  Scenario: Position creation rejects a blank title
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user submits the blank Position form
    Then the Position title fails browser validation

  Scenario: Position creation requires a department
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user submits a Position without a department
    Then the Position department fails browser validation

  Scenario: Position title honors its minimum length
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user enters a one-character Position title
    Then the Position title fails browser validation

  Scenario: Position creation rejects zero interview questions
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user submits a Position with zero interview questions
    Then the Position question count fails browser validation

  Scenario: Position description honors its minimum length
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user enters a short Position description
    Then the Position description fails browser validation

  Scenario: Company user can create a unique automation Position
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user creates a unique automation Position
    Then the created Position is visible

  Scenario: A created Position is Active
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user creates a unique automation Position
    Then the created Position is Active

  Scenario: A created Position preserves its description
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user creates a unique automation Position
    Then the created Position displays its automation description

  Scenario: A created Position displays every submitted skill
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user creates a unique automation Position
    Then the created Position displays all submitted skills

  Scenario: Company user can open an automation Position for editing
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user creates a unique automation Position
    And the Company user opens the created Position for editing
    Then the Edit position form is displayed

  Scenario: Company user can update an automation Position title
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user creates a unique automation Position
    And the Company user opens the created Position for editing
    And the Company user updates the created Position title
    Then the updated Position title is visible

  Scenario: Company user can archive an automation Position
    Given an authenticated Company user opens the Positions page
    When the Company user opens the New position form
    And the Company user creates a unique automation Position
    And the Company user archives the created Position
    Then the created Position is Archived
