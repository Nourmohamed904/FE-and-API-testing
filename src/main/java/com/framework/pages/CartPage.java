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

    private final By cartTableRows = By.cssSelector(".table-bordered tbody tr");
    private final By productNameColumn = By.cssSelector(".text-left a");
    private final By checkoutButton = By.linkText("Checkout");
    private final By cartSubtotal = By.cssSelector(".table-bordered tfoot tr:first-child td:last-child");
    private final By cartTotalValue = By.cssSelector(".table-bordered tfoot tr:last-child td:last-child");
    private final By shoppingCartHeading = By.cssSelector("#content h1");

    public boolean isProductInCart(String productName) {
        List<WebElement> rows = driver.findElements(cartTableRows);
        for (WebElement row : rows) {
            String name = row.findElement(productNameColumn).getText();
            if (name.equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }

    public String getProductPrice(String productName) {
        List<WebElement> rows = driver.findElements(cartTableRows);
        for (WebElement row : rows) {
            if (row.findElement(productNameColumn).getText().equalsIgnoreCase(productName)) {
                List<WebElement> priceCells = row.findElements(By.cssSelector(".text-right"));
                return priceCells.size() > 0 ? priceCells.get(0).getText() : null;
            }
        }
        return null;
    }

    public String getProductPriceInCart(String productName) {
        return getProductPrice(productName);
    }

    public String getCartSubtotal() {
        return getText(cartSubtotal);
    }

    public String getCartTotalValue() {
        return getText(cartTotalValue);
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutPage(driver);
    }

    public int getCartItemCount() {
        return driver.findElements(cartTableRows).size();
    }

    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }

    public boolean isCartPageDisplayed() {
        return isDisplayed(shoppingCartHeading);
    }
}