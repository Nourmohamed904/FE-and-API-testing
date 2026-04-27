package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    // Cart table locators
    private final By cartTableRows = By.cssSelector(".table-bordered tbody tr, .table tbody tr");
    private final By productNameColumn = By.xpath(".//td[@class='text-left']/a");
    private final By priceColumn = By.xpath(".//td[@class='text-right']");
    private final By totalPrice = By.xpath("//table[@class='table table-bordered']//tfoot//td[contains(text(),'Total')]/following-sibling::td");
    private final By checkoutButton = By.linkText("Checkout");
    private final By removeButton = By.cssSelector(".btn-danger");
    private final By emptyCartMessage = By.cssSelector("#content p");

    // NEW: Remove button for each row - more robust
    private final By removeButtons = By.cssSelector("button[onclick*='cart.remove']");

    // NEW: Warning message for out of stock items
    private final By warningMessage = By.cssSelector(".alert-danger");

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

    public boolean hasOutOfStockWarning() {
        return areElementsPresent(warningMessage);
    }

    public String getWarningMessage() {
        return areElementsPresent(warningMessage) ? getText(warningMessage) : "";
    }

    // ========== NEW METHOD: Clear all items from cart ==========
    public CartPage clearCart() {
        List<WebElement> removeButtonsList = driver.findElements(removeButtons);

        if (removeButtonsList.isEmpty()) {
            // Cart is already empty
            return this;
        }

        // Keep removing until cart is empty
        int maxAttempts = 10;
        int attempts = 0;

        while (getCartItemCount() > 0 && attempts < maxAttempts) {
            removeButtonsList = driver.findElements(removeButtons);
            if (!removeButtonsList.isEmpty()) {
                try {
                    // Click the first remove button
                    removeButtonsList.get(0).click();
                    // Wait for cart to update
                    Thread.sleep(1000);
                } catch (Exception e) {
                    break;
                }
            }
            attempts++;
        }

        return this;
    }

    // NEW: Remove specific product from cart
    public CartPage removeProduct(String productName) {
        List<WebElement> rows = driver.findElements(cartTableRows);

        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            try {
                WebElement nameElement = row.findElement(productNameColumn);
                if (nameElement.getText().toLowerCase().contains(productName.toLowerCase())) {
                    // Find remove button in this row
                    WebElement removeBtn = row.findElement(By.cssSelector(".btn-danger"));
                    removeBtn.click();
                    Thread.sleep(1000);
                    break;
                }
            } catch (Exception e) {
                // Continue to next row
            }
        }

        return this;
    }

    // NEW: Get all product names in cart
    public List<String> getAllProductNamesInCart() {
        List<String> productNames = new ArrayList<>();
        List<WebElement> rows = driver.findElements(cartTableRows);

        for (WebElement row : rows) {
            try {
                WebElement nameElement = row.findElement(productNameColumn);
                productNames.add(nameElement.getText());
            } catch (Exception e) {
                // Skip
            }
        }

        return productNames;
    }
}