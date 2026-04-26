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
    private final By productNameColumn = By.xpath(".//td[@class='text-left']/a");
    private final By priceColumn = By.xpath(".//td[@class='text-right']");
    private final By totalPrice = By.xpath("//table[@class='table table-bordered']//tfoot//td[contains(text(),'Total')]/following-sibling::td");
    private final By checkoutButton = By.linkText("Checkout");
    private final By removeButton = By.cssSelector(".btn-danger");
    private final By quantityField = By.cssSelector(".form-control");
    private final By updateButton = By.cssSelector(".btn-primary");

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
                return row.findElement(priceColumn).getText();
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

    public int getCartItemCount() {
        return driver.findElements(cartTableRows).size();
    }

    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }
}