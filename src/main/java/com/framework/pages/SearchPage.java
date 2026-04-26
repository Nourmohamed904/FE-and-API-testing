package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.util.ArrayList;
import java.util.List;

public class SearchPage extends BasePage {

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    // Product listing elements
    private final By productItems = By.cssSelector(".product-layout");
    private final By productNames = By.cssSelector(".product-layout h4 a");
    private final By productPrices = By.cssSelector(".price");
    private final By noResultsMessage = By.cssSelector("#content p");

    // Sorting elements
    private final By sortDropdown = By.id("input-sort");

    // Breadcrumb elements
    private final By activeBreadcrumb = By.cssSelector(".breadcrumb li.active");

    // Left side menu
    private final By leftSideMenu = By.cssSelector(".list-group a");
    private final By activeLeftMenuItem = By.cssSelector(".list-group a.active");

    // Navigation methods (to navigate to categories)
    private final By desktopsMenu = By.linkText("Desktops");
    private final By showAllDesktops = By.linkText("Show All Desktops");
    private final By tabletsMenu = By.linkText("Tablets");
    private final By phonesMenu = By.linkText("Phones & PDAs");
    private final By mp3PlayersMenu = By.linkText("MP3 Players");
    private final By showAllMP3Players = By.linkText("Show All MP3 Players");

    // Category navigation methods
    public SearchPage goToDesktops() {
        click(desktopsMenu);
        click(showAllDesktops);
        return this;
    }

    public SearchPage goToTablets() {
        click(tabletsMenu);
        return this;
    }

    public SearchPage goToPhonesAndPDAs() {
        click(phonesMenu);
        return this;
    }

    public SearchPage goToMP3Players() {
        click(mp3PlayersMenu);
        click(showAllMP3Players);
        return this;
    }

    // Search result methods
    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> productElements = waitForElements(productNames);
        for (WebElement product : productElements) {
            names.add(product.getText());
        }
        return names;
    }

    public List<String> getAllProductPrices() {
        List<String> prices = new ArrayList<>();
        List<WebElement> priceElements = waitForElements(productPrices);
        for (WebElement price : priceElements) {
            prices.add(price.getText());
        }
        return prices;
    }

    public boolean hasResults() {
        return areElementsPresent(productItems);
    }

    public String getNoResultsMessage() {
        return getText(noResultsMessage);
    }

    // Sorting methods
    public SearchPage sortBy(String sortOption) {
        Select sortSelect = new Select(waitForElement(sortDropdown));
        sortSelect.selectByVisibleText(sortOption);
        wait.until(d -> getElementCount(productItems) > 0);
        return this;
    }

    public SearchPage sortByNameAZ() {
        return sortBy("Name (A - Z)");
    }

    public SearchPage sortByNameZA() {
        return sortBy("Name (Z - A)");
    }

    public boolean isSortedAscending(List<String> items) {
        for (int i = 0; i < items.size() - 1; i++) {
            if (items.get(i).compareToIgnoreCase(items.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isSortedDescending(List<String> items) {
        for (int i = 0; i < items.size() - 1; i++) {
            if (items.get(i).compareToIgnoreCase(items.get(i + 1)) < 0) {
                return false;
            }
        }
        return true;
    }

    // Add to cart from search results
    public SearchPage addProductToCart(String productName) {
        List<WebElement> products = waitForElements(productItems);
        for (WebElement product : products) {
            String name = product.findElement(productNames).getText();
            if (name.equalsIgnoreCase(productName)) {
                WebElement addToCart = product.findElement(By.cssSelector("button[onclick*='cart.add']"));
                addToCart.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
                break;
            }
        }
        return this;
    }

    // Click product to go to ProductPage
    public ProductPage clickProduct(String productName) {
        List<WebElement> products = waitForElements(productNames);
        for (WebElement product : products) {
            if (product.getText().equalsIgnoreCase(productName)) {
                product.click();
                return new ProductPage(driver);
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    // Breadcrumb methods
    public String getActiveBreadcrumb() {
        return getText(activeBreadcrumb);
    }

    public boolean isBreadcrumbCorrect(String expectedLastCrumb) {
        return getActiveBreadcrumb().equalsIgnoreCase(expectedLastCrumb);
    }

    // Left side menu methods
    public String getActiveLeftMenuItem() {
        return getText(activeLeftMenuItem);
    }

    public boolean isLeftMenuItemHighlighted(String expectedItem) {
        return getActiveLeftMenuItem().equalsIgnoreCase(expectedItem);
    }

    // Currency change verification
    public boolean havePricesChanged(List<String> oldPrices) {
        List<String> newPrices = getAllProductPrices();
        return !oldPrices.equals(newPrices);
    }

    public int getProductCount() {
        return getElementCount(productItems);
    }
}