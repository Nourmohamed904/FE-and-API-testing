package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage {
    private static final String DEFAULT_DELIVERY_DATE = "2026-05-10";

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    private final By addToCartButton = By.id("button-cart");
    private final By deliveryDateField = By.cssSelector("input[name='date']");
    private final By successAlert = By.cssSelector(".alert-success");

    public ProductPage addToCart() {
        populateDeliveryDateIfRequired(DEFAULT_DELIVERY_DATE);
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        scrollToElement(addToCartButton);

        try {
            click(addToCartButton);
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", driver.findElement(addToCartButton));
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        return this;
    }

    public ProductPage addToCart(String deliveryDate) {
        populateDeliveryDateIfRequired(deliveryDate);
        return addToCart();
    }

    public String getSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        return getText(successAlert);
    }

    private void populateDeliveryDateIfRequired(String deliveryDate) {
        if (!areElementsPresent(deliveryDateField)) {
            return;
        }

        String value = deliveryDate == null || deliveryDate.isBlank() ? DEFAULT_DELIVERY_DATE : deliveryDate;
        org.openqa.selenium.WebElement dateInput = waitForElement(deliveryDateField);
        dateInput.clear();
        dateInput.sendKeys(value);
    }

    private void scrollToElement(By locator) {
        org.openqa.selenium.JavascriptExecutor js =
                (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(locator));
    }
}
