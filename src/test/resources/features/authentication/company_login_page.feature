Feature: Company login page

  @ui
  Scenario: Company user opens the login page
    Given the Company application is available
    When the Company user opens the login page
    Then the Company login page should be displayed
    And the Company login email and password fields should be displayed
    And the Company login sign in button should be displayed

  @ui
  @authentication
  @negative
  Scenario: Company user signs in with invalid credentials
    Given the Company user is on the login page
    When the Company user signs in with invalid credentials
    Then the Company login should be rejected
    And the Company user should remain on the login page

  @ui
  @authentication
  @negative
  Scenario Outline: Company login rejects blank email submissions
    Given the Company user is on the login page
    When the Company user submits email "<email>" and password "<password>"
    Then the Company login error should be "Enter a valid email address."
    And the Company user should remain on the login page

    Examples:
      | email | password            |
      |       |                     |
      |       | PlausiblePassword1! |

  @ui
  @authentication
  @negative
  Scenario: Company login prevents submission of an invalid email format
    Given the Company user is on the login page
    When the Company user submits email "invalid-email" and password "PlausiblePassword1!"
    Then the Company email value should fail browser validation
    And the Company user should remain on the login page

  @ui
  @authentication
  @negative
  Scenario Outline: Company login rejects invalid credential combinations
    Given the Company user is on the login page
    When the Company user submits email "<email>" and password "<password>"
    Then the Company login should display a rejection error
    And the Company user should remain on the login page

    Examples:
      | email                  | password             |
      | person@example.com     |                      |
      | wrong@example.com      | PlausiblePassword1!  |
      | automation@example.com | IncorrectPassword1!  |

  @ui
  @authentication
  Scenario: Company login credential fields use appropriate input types
    Given the Company user is on the login page
    Then the Company credential fields should use secure input types

  @ui
  @authentication
  @protectedRoutes
  Scenario Outline: Unauthenticated Company user is redirected from a protected route
    Given the unauthenticated Company user opens protected route "<route>"
    Then the Company user should be redirected to the login page

    Examples:
      | route            |
      | /app             |
      | /app/candidates  |
      | /app/positions   |
      | /app/interviews  |
      | /app/team        |
      | /app/audit       |
      | /app/settings    |

  @ui
  @authentication
  @positive
  @requiresCredentials
  Scenario: Company user signs in with valid credentials
    Given the Company user is on the login page
    When the Company user signs in with valid credentials
    Then the Company user should enter the authenticated workspace
    And the Company Overview page should be displayed
    And the Company user should no longer be on the login page
