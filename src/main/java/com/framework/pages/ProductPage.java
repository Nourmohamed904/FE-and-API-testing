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
        wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        return getText(successAlert);
    }
}