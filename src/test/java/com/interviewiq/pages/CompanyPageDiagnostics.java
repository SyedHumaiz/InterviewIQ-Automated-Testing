package com.interviewiq.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/** Safe, failure-only diagnostics for unexpected protected-route navigation. */
final class CompanyPageDiagnostics {
    private CompanyPageDiagnostics() {
    }

    static String snapshot(WebDriver driver) {
        String url = driver.getCurrentUrl();
        return "url=" + url
                + ", path=" + URI.create(url).getPath()
                + ", title=" + driver.getTitle()
                + ", visibleH1=" + visibleText(driver, By.tagName("h1"))
                + ", loginHeadingPresent=" + isVisible(driver, By.xpath("//h2[normalize-space()='Sign in to your workspace']"))
                + ", overviewHeadingPresent=" + isVisible(driver, By.xpath("//h1[normalize-space()='Overview']"))
                + ", candidatesNavPresent=" + isVisible(driver, By.cssSelector("a[href='/app/candidates']"))
                + ", positionsNavPresent=" + isVisible(driver, By.cssSelector("a[href='/app/positions']"));
    }

    private static boolean isVisible(WebDriver driver, By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }

    private static String visibleText(WebDriver driver, By locator) {
        List<String> text = driver.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .filter(value -> !value.isBlank())
                .collect(Collectors.toList());
        return text.toString();
    }
}
