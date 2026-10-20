# InterviewIQ Automated Testing

An end-to-end automated test suite for InterviewIQ. The project validates the web applications and backend APIs with Java, Maven, Selenium, Cucumber, JUnit, RestAssured, and a Page Object Model structure.

## Objective

Provide repeatable regression coverage for critical InterviewIQ workflows while keeping test credentials and session tokens outside source control.

## Testing scope

The suite contains **182 executable automated Cucumber scenarios** covering:

- Authentication and session management
- Authorization and role-based access control (RBAC)
- Candidate management
- Position management
- Resume validation and ownership
- Invitations
- Interviews
- Company settings
- Admin management
- API and security testing
- End-to-end regression

## Technologies

- Java 17
- Maven
- Selenium WebDriver
- Cucumber
- JUnit
- RestAssured
- Page Object Model
- GitHub

## Project structure

```text
.
|- pom.xml
|- src/
|  |- main/java/                 Application entry point
|  `- test/
|     |- java/com/interviewiq/
|     |  |- api/                 REST client and authenticated API session support
|     |  |- config/              Runtime configuration
|     |  |- hooks/               Shared Cucumber hooks and session lifecycle
|     |  |- pages/               Selenium Page Objects
|     |  |- runners/             Cucumber/JUnit suite runner
|     |  `- stepdefinitions/    Step implementations
|     `- resources/
|        |- config/              Non-secret endpoint configuration
|        |- features/            Cucumber feature files
|        `- fixtures/            Test fixtures
`- README.md
```

## Build and run

Compile the project:

```powershell
mvn clean test-compile
```

Run an unfiltered Cucumber dry run (validates step definitions without executing browser/API calls):

```powershell
mvn --% test -Dcucumber.filter.tags= -Dcucumber.execution.dry-run=true
```

Run the credential-free test set (the default excludes `@requiresCredentials` scenarios):

```powershell
mvn test
```

Provide Company and Admin credentials only at execution time, then run the full suite:

```powershell
mvn --% test -Dcucumber.filter.tags= -Dcompany.test.email=YOUR_COMPANY_EMAIL -Dcompany.test.password=YOUR_COMPANY_PASSWORD -Dadmin.test.email=YOUR_ADMIN_EMAIL -Dadmin.test.password=YOUR_ADMIN_PASSWORD
```

An API session token is optional. Supply it only when needed instead of using the Company login flow:

```powershell
mvn test -Dcompany.api.token="YOUR_COMPANY_API_TOKEN"
```

The Surefire and Cucumber output is generated under `target/` (including `target/surefire-reports/` where applicable). This directory is intentionally ignored by Git.

## Validated status

- 182 scenarios implemented
- 0 failures / 0 errors in the latest validated credentialed regression subset
- BUILD SUCCESS

Credentials and API tokens are intentionally not stored in this repository. Supply test credentials through JVM properties; `COMPANY_API_TOKEN` is also supported as an environment variable for the optional Company API token.

## Contributors

- Syed Humaiz Hussain Zaidi
- Shahan Ahmed Nasir
- Arhab Umer
- Ruhan Khan
- Codex, an AI coding/development assistant
