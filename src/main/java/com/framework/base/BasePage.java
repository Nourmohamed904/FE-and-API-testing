package com.framework.base;

import com.framework.pages.components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected HeaderComponent header;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.header = new HeaderComponent(driver);
    }

    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> waitForElements(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    // In BasePage.java, update the type method:
    protected void type(By locator, String text) {
        if (text == null || text.equals("null") || text.isEmpty()) {
            return;  // Don't type anything if text is null or empty
        }
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForElement(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitForElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean areElementsPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    protected int getElementCount(By locator) {
        return driver.findElements(locator).size();
    }

    public HeaderComponent header() {
        return header;
    }

    // Add this method to BasePage.java
    protected boolean isElementPresentFast(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}