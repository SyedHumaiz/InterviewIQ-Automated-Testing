Feature: Platform Admin public authentication

  @ui @admin
  Scenario: Platform Admin login page exposes credentials
    Given the Platform Admin public application is available
    When the Platform Admin opens the login page
    Then the Platform Admin login page is displayed
    And the Platform Admin login credentials are displayed

  @ui @admin
  Scenario: Platform Admin login fields use secure input types
    Given the Platform Admin public application is available
    When the Platform Admin opens the login page
    Then the Platform Admin login fields use secure types

  @ui @admin @negative
  Scenario: Platform Admin login rejects a blank email
    Given the Platform Admin public application is available
    When the Platform Admin opens the login page
    And the Platform Admin submits login email "" and password "PlausiblePassword1!"
    Then the Platform Admin login error is "Enter a valid email address."

  @ui @admin @negative
  Scenario: Platform Admin login rejects malformed email
    Given the Platform Admin public application is available
    When the Platform Admin opens the login page
    And the Platform Admin submits login email "invalid-email" and password "PlausiblePassword1!"
    Then the Platform Admin login error is "Enter a valid email address."

  @ui @admin
  Scenario: Platform Admin signup page exposes its four fields
    Given the Platform Admin public application is available
    When the Platform Admin opens the signup page
    Then the Platform Admin signup page and fields are displayed

  @ui @admin @negative
  Scenario: Platform Admin signup validates missing identity
    Given the Platform Admin public application is available
    When the Platform Admin opens the signup page
    And the Platform Admin submits signup name "", email "not-an-email", password "secret1", and confirmation "secret1"
    Then the Platform Admin signup error is "Enter your name and a valid email address."

  @ui @admin @negative
  Scenario: Platform Admin signup validates short password
    Given the Platform Admin public application is available
    When the Platform Admin opens the signup page
    And the Platform Admin submits signup name "Automation Admin", email "automation-admin@example.com", password "short", and confirmation "short"
    Then the Platform Admin signup error is "Password must contain at least 6 characters."

  @ui @admin @negative
  Scenario: Platform Admin signup validates password confirmation
    Given the Platform Admin public application is available
    When the Platform Admin opens the signup page
    And the Platform Admin submits signup name "Automation Admin", email "automation-admin@example.com", password "secret1", and confirmation "secret2"
    Then the Platform Admin signup error is "Passwords do not match."

  @ui @admin @protectedRoutes
  Scenario Outline: Unauthenticated Platform Admin is redirected from protected routes
    Given the unauthenticated Platform Admin opens protected route "<route>"
    Then the Platform Admin is redirected to login

    Examples:
      | route |
      | /app |
      | /app/companies |
      | /app/admins |
      | /app/audit |
      | /app/settings |

  @ui @admin @requiresCredentials @requiresAdminCredentials
  Scenario: Platform Admin valid credentials reach the dashboard
    Given the Platform Admin signs in with valid configured credentials
    Then the Platform Admin dashboard is displayed
