package com.interviewiq.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverManager {

    private static WebDriver driver;

    private DriverManager() {
    }

    public static void initializeChromeDriver() {
        if (driver == null) {
            driver = new ChromeDriver(new ChromeOptions());
        }
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialized.");
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }
}
