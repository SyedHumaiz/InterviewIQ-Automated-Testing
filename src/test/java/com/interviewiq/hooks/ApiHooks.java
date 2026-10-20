package com.interviewiq.hooks;

import com.interviewiq.api.CompanyApiSession;
import io.cucumber.java.Before;

public class ApiHooks {
    @Before("@apiCompanySession")
    public void startSharedCompanyApiSession() { CompanyApiSession.start(); }
}
