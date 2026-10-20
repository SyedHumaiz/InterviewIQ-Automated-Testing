package com.interviewiq.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/** Minimal shared client for the deployed InterviewIQ HTTP API. */
public final class ApiClient {
    public static final String BASE_URL = "https://fyp-backend-tau-pied.vercel.app";
    private ApiClient() { }
    public static RequestSpecification anonymous() { return RestAssured.given().baseUri(BASE_URL); }
    public static Response getAnonymous(String path) { return anonymous().get(path); }
}
