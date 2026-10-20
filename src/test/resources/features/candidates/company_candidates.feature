@ui
@candidates
@requiresCredentials
Feature: Company candidate management

  Scenario: Authenticated Company user can open Candidates
    Given an authenticated Company user opens the Candidates page
    Then the Candidates page heading is displayed

  Scenario: Candidates page provides an Add candidate control
    Given an authenticated Company user opens the Candidates page
    Then the Add candidate control is available

  Scenario: Candidates page shows a list or empty state
    Given an authenticated Company user opens the Candidates page
    Then the candidate list or its empty state is displayed

  Scenario: Company user can open the Add candidate form
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    Then the Add candidate form is displayed

  Scenario: Candidate creation requires a full name
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user submits the candidate form without a name
    Then the candidate name fails browser validation

  Scenario: Candidate creation requires an email address
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user submits the candidate form without an email
    Then the candidate email fails browser validation

  Scenario: Candidate creation rejects an invalid email address
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user enters an invalid candidate email
    Then the candidate email fails browser validation

  Scenario: Candidate creation requires a resume
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user submits otherwise valid candidate data without a resume
    Then candidate creation is rejected with the missing resume message

  Scenario: Company user can create a candidate with unique valid data
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user creates a unique automation candidate
    Then candidate creation is confirmed for the created candidate

  Scenario: A newly created candidate appears in the candidate listing
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user creates a unique automation candidate
    And the Company user returns to the candidate listing
    Then the created candidate is visible in the listing

  Scenario: Candidate search finds a newly created candidate
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user creates a unique automation candidate
    And the Company user returns to the candidate listing
    And the Company user searches for the created candidate
    Then the created candidate is visible in the listing

  Scenario: Candidate search reports no matches for an unknown candidate
    Given an authenticated Company user opens the Candidates page
    When the Company user searches for a nonexistent candidate
    Then no candidates match the search

  Scenario: Active filter includes a newly created candidate
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user creates a unique automation candidate
    And the Company user returns to the candidate listing
    And the Company user filters candidates by "Active"
    Then the created candidate is visible in the listing

  Scenario: Company user can archive an automation candidate
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user creates a unique automation candidate
    And the Company user returns to the candidate listing
    And the Company user archives the created candidate
    Then the created candidate is shown as archived

  Scenario: Archived filter includes an archived automation candidate
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user creates a unique automation candidate
    And the Company user returns to the candidate listing
    And the Company user archives the created candidate
    And the Company user filters candidates by "Archived"
    Then the created candidate is visible in the listing

  Scenario: Clearing the status filter restores the created candidate
    Given an authenticated Company user opens the Candidates page
    When the Company user opens the Add candidate form
    And the Company user creates a unique automation candidate
    And the Company user returns to the candidate listing
    And the Company user filters candidates by "Active"
    And the Company user filters candidates by "All candidates"
    Then the created candidate is visible in the listing
