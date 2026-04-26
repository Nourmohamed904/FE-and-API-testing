package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class CartPage extends BasePage {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    private final By cartItems = By.cssSelector(".table-bordered tbody tr");
    private final By itemName = By.cssSelector(".text-left a");
    private final By itemPrice = By.cssSelector(".text-right:not(:last-child)");
    private final By itemDeliveryDate = By.cssSelector(".text-left small");
    private final By totalPrice = By.xpath("//table[@class='table table-bordered']//tfoot//td[contains(text(),'Total')]/following-sibling::td");
    private final By checkoutButton = By.linkText("Checkout");
    private final By removeButton = By.cssSelector(".btn-danger");
    private final By updateCartButton = By.cssSelector(".btn-primary");
    private final By continueShoppingButton = By.linkText("Continue Shopping");
    private final By emptyCartMessage = By.cssSelector("#content p");

    public boolean isProductInCart(String productName) {
        List<WebElement> items = driver.findElements(itemName);
        for (WebElement item : items) {
            if (item.getText().contains(productName)) {
                return true;
            }
        }
        return false;
    }

    public String getProductPrice(String productName) {
        // Find the row containing the product and get its price
        List<WebElement> rows = driver.findElements(cartItems);
        for (WebElement row : rows) {
            if (row.findElement(itemName).getText().contains(productName)) {
                return row.findElement(itemPrice).getText();
            }
        }
        return null;
    }

    public String getDeliveryDate(String productName) {
        List<WebElement> rows = driver.findElements(cartItems);
        for (WebElement row : rows) {
            if (row.findElement(itemName).getText().contains(productName)) {
                WebElement dateElement = row.findElement(itemDeliveryDate);
                return dateElement != null ? dateElement.getText() : null;
            }
        }
        return null;
    }

    public String getTotalPrice() {
        return getText(totalPrice);
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutPage(driver);
    }

    public CartPage removeProduct(String productName) {
        List<WebElement> rows = driver.findElements(cartItems);
        for (WebElement row : rows) {
            if (row.findElement(itemName).getText().contains(productName)) {
                row.findElement(removeButton).click();
                wait.until(ExpectedConditions.stalenessOf(row));
                break;
            }
        }
        return this;
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public boolean isCartEmpty() {
        return getCartItemCount() == 0 || isDisplayed(emptyCartMessage);
    }

    public String getEmptyCartMessage() {
        return getText(emptyCartMessage);
    }
}