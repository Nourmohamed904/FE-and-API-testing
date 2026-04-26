package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage {

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    private final By addToCartButton = By.id("button-cart");
    private final By successAlert = By.cssSelector(".alert-success");

    public ProductPage addToCart() {
        click(addToCartButton);
        return this;
    }

    public String getSuccessMessage() {
        // Wait for success alert to disappear and get message
        wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        String message = getText(successAlert);
        return message;
    }

    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successAlert);
    }

    public String getSuccessMessageText() {
        return getText(successAlert);
    }
}