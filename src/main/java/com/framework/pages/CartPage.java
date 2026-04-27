package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends HomePage {
    private static final int MAX_CLEAR_ATTEMPTS = 20;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    private final By cartTableRows = By.cssSelector(".table-bordered tbody tr, .table tbody tr");
    private final By productNameColumn = By.xpath(".//td[@class='text-left']/a");
    private final By priceColumn = By.xpath(".//td[@class='text-right']");
    private final By totalPrice = By.xpath("//table[@class='table table-bordered']//tfoot//td[contains(text(),'Total')]/following-sibling::td");
    private final By emptyCartMessage = By.cssSelector("#content p");

    private final By removeButtons = By.cssSelector("button[onclick*='cart.remove']");
    private final By warningMessage = By.cssSelector(".alert-danger");
    private final By cartContent = By.id("content");

    public boolean isProductInCart(String productName) {
        try {
            List<WebElement> rows = driver.findElements(cartTableRows);
            for (WebElement row : rows) {
                try {
                    WebElement nameElement = row.findElement(productNameColumn);
                    String name = nameElement.getText();
                    if (name.toLowerCase().contains(productName.toLowerCase())) {
                        return true;
                    }
                } catch (Exception e) {
                    // Skip rows without product name
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTotalPrice() {
        try {
            return getText(totalPrice);
        } catch (Exception e) {
            return "Not found";
        }
    }

    public int getCartItemCount() {
        try {
            return driver.findElements(cartTableRows).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clearCart() {
        waitForElement(cartContent);

        int attempts = 0;

        while (getCartItemCount() > 0 && attempts < MAX_CLEAR_ATTEMPTS) {
            List<WebElement> removeButtonsList = driver.findElements(removeButtons);

            if (removeButtonsList.isEmpty()) {
                break;
            }

            try {
                WebElement removeBtn = removeButtonsList.get(0);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeBtn);
                wait.until(ExpectedConditions.stalenessOf(removeBtn));
            } catch (Exception e) {
                break;
            }
            attempts++;
        }

        driver.navigate().refresh();
        waitForElement(cartContent);

    }
}
