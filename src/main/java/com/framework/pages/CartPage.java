package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class CartPage extends BasePage {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    // More robust locators for the cart table
    private final By cartTableRows = By.cssSelector(".table-bordered tbody tr, .table tbody tr");
    private final By productNameColumn = By.xpath(".//td[@class='text-left']/a");
    private final By priceColumn = By.xpath(".//td[@class='text-right']");
    private final By totalPrice = By.xpath("//table[@class='table table-bordered']//tfoot//td[contains(text(),'Total')]/following-sibling::td");
    private final By checkoutButton = By.linkText("Checkout");
    private final By removeButton = By.cssSelector(".btn-danger");
    private final By emptyCartMessage = By.cssSelector("#content p");

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

    public String getProductPrice(String productName) {
        try {
            List<WebElement> rows = driver.findElements(cartTableRows);
            for (WebElement row : rows) {
                try {
                    WebElement nameElement = row.findElement(productNameColumn);
                    if (nameElement.getText().toLowerCase().contains(productName.toLowerCase())) {
                        return row.findElement(priceColumn).getText();
                    }
                } catch (Exception e) {
                    // Skip
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getTotalPrice() {
        try {
            return getText(totalPrice);
        } catch (Exception e) {
            return "Not found";
        }
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutPage(driver);
    }

    public int getCartItemCount() {
        try {
            return driver.findElements(cartTableRows).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isCartEmpty() {
        return getCartItemCount() == 0 || isDisplayed(emptyCartMessage);
    }

    public String getEmptyCartMessage() {
        return getText(emptyCartMessage);
    }
}