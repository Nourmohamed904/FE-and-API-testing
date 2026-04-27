package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {
    private static final int MAX_CLEAR_ATTEMPTS = 20;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    private final By cartTableRows = By.cssSelector(".table-bordered tbody tr, .table tbody tr");
    private final By productNameColumn = By.xpath(".//td[@class='text-left']/a");
    private final By priceColumn = By.xpath(".//td[@class='text-right']");
    private final By totalPrice = By.xpath("//table[@class='table table-bordered']//tfoot//td[contains(text(),'Total')]/following-sibling::td");
    private final By checkoutButton = By.linkText("Checkout");
    private final By removeButton = By.cssSelector(".btn-danger");
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

    public CartPage clearCart() {
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

        return this;
    }

    public CartPage removeProduct(String productName) {
        List<WebElement> rows = driver.findElements(cartTableRows);

        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            try {
                WebElement nameElement = row.findElement(productNameColumn);
                if (nameElement.getText().toLowerCase().contains(productName.toLowerCase())) {
                    WebElement removeBtn = row.findElement(By.cssSelector(".btn-danger"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeBtn);
                    wait.until(ExpectedConditions.stalenessOf(row));
                    break;
                }
            } catch (Exception e) {
                // Try the next row if this one changed while the cart refreshed.
            }
        }

        return this;
    }

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
