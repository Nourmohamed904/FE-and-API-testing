package com.framework.pages;

import com.framework.pages.components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private static final int DEFAULT_WAIT_SECONDS = 15;

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final HeaderComponent header;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
        this.header = new HeaderComponent(driver);
    }

    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void type(By locator, String text) {
        if (text == null || text.trim().isEmpty()) {
            return;
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

    public HeaderComponent header() {
        return header;
    }

    public void hover(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Actions(driver).moveToElement(element).perform();
    }

    public void hoverAndClick(By hoverLocator, By clickLocator) {
        hover(hoverLocator);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(clickLocator));

        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void waitForDocumentReady() {
        wait.until(driver -> "complete".equals(
                ((JavascriptExecutor) driver).executeScript("return document.readyState")));
    }
}
