package com.interviewiq.api;

import com.interviewiq.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** One safely reused HTTP cookie session for credential-dependent API scenarios. */
public final class CompanyApiSession {
    /** Backend contract: Company authentication is the HttpOnly token cookie. */
    private static final String COMPANY_SESSION_COOKIE = "token";
    private static final String TOKEN_PROPERTY = "company.api.token";
    private static final String TOKEN_ENVIRONMENT_VARIABLE = "COMPANY_API_TOKEN";
    private static volatile String token;
    private static volatile boolean attempted;
    private static volatile boolean initialized;
    private static volatile Throwable initializationFailure;

    private CompanyApiSession() { }

    public static synchronized void start() {
        if (initialized) return;
        if (attempted) rethrowInitializationFailure();

        attempted = true;
        try {
            initializeOnce();
            initialized = true;
        } catch (Throwable failure) {
            initializationFailure = failure;
            rethrow(failure);
        }
    }

    private static void initializeOnce() {
        String suppliedToken = configuredToken();
        if (suppliedToken != null) {
            verifySession(suppliedToken, "Configured Company API token is invalid or expired");
            token = suppliedToken;
            return;
        }

        Response response = ApiClient.anonymous().contentType("application/json")
                .body("{\"email\":\"" + json(ConfigManager.getCompanyTestEmail())
                        + "\",\"password\":\"" + json(ConfigManager.getCompanyTestPassword()) + "\"}")
                .post("/api/company-auth/login");
        assertEquals(200, response.statusCode(), "Company API login failed: " + safeDiagnostics(response));

        Cookie companySession = response.getDetailedCookie(COMPANY_SESSION_COOKIE);
        if (companySession == null || companySession.getValue() == null || companySession.getValue().isBlank()) {
            throw new IllegalStateException("Company API login did not return " + COMPANY_SESSION_COOKIE
                    + "; " + safeDiagnostics(response));
        }
        String candidateToken = companySession.getValue();
        verifySession(candidateToken, "Company API session verification failed");
        token = candidateToken;
    }

    public static RequestSpecification authenticated() {
        if (!initialized || token == null) throw new IllegalStateException("Company API session has not started.");
        return newAuthenticatedRequest(token);
    }

    private static void verifySession(String candidateToken, String failureMessage) {
        Response me = newAuthenticatedRequest(candidateToken).get("/api/company-auth/me");
        assertEquals(200, me.statusCode(), failureMessage + ": " + safeDiagnostics(me));
    }

    private static String configuredToken() {
        String value = System.getProperty(TOKEN_PROPERTY);
        if (value == null || value.isBlank()) value = System.getenv(TOKEN_ENVIRONMENT_VARIABLE);
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static void rethrowInitializationFailure() {
        Throwable failure = initializationFailure;
        if (failure == null) {
            throw new IllegalStateException("Company API session initialization previously failed.");
        }
        rethrow(failure);
    }

    private static void rethrow(Throwable failure) {
        if (failure instanceof RuntimeException runtimeException) throw runtimeException;
        if (failure instanceof Error error) throw error;
        throw new IllegalStateException("Company API session initialization failed.", failure);
    }

    private static RequestSpecification newAuthenticatedRequest(String sessionToken) {
        return RestAssured.given().baseUri(ApiClient.BASE_URL)
                .cookie(COMPANY_SESSION_COOKIE, sessionToken);
    }
    private static String json(String value) { return value.replace("\\", "\\\\").replace("\"", "\\\""); }
    private static String safeDiagnostics(Response response) {
        String cookieNames = response.detailedCookies().asList().stream()
                .map(Cookie::getName).collect(Collectors.joining(","));
        String message;
        try { message = response.jsonPath().getString("message"); }
        catch (Exception ignored) { message = null; }
        return "status=" + response.statusCode() + ", cookieNames=[" + cookieNames + "]"
                + (message == null || message.isBlank() ? "" : ", message=" + message);
    }
}
