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

    // Product listing
    private final By productItems = By.cssSelector(".product-layout");
    private final By productNames = By.cssSelector(".product-layout .caption h4 a");
    private final By productPrices = By.cssSelector(".product-layout .price");
    private final By noResultsMessage = By.cssSelector("#content p");

    // Sorting
    private final By sortDropdown = By.id("input-sort");

    // Breadcrumb
    private final By activeBreadcrumb = By.cssSelector(".breadcrumb li:last-child");

    // Left menu
    private final By activeLeftMenuItem = By.cssSelector(".list-group a.active");

    // Navigation menus - FIXED based on actual HTML
    private final By desktopsMenu = By.xpath("//ul[@class='nav navbar-nav']//a[contains(text(), 'Desktops')]");
    private final By showAllDesktops = By.xpath("//a[contains(@class, 'see-all') and contains(text(), 'Show AllDesktops')]");

    private final By laptopsMenu = By.xpath("//ul[@class='nav navbar-nav']//a[contains(text(), 'Laptops & Notebooks')]");
    private final By showAllLaptops = By.xpath("//a[contains(@class, 'see-all') and contains(text(), 'Show AllLaptops & Notebooks')]");

    private final By tabletsMenu = By.linkText("Tablets");

    private final By phonesMenu = By.linkText("Phones & PDAs");

    private final By mp3PlayersMenu = By.xpath("//ul[@class='nav navbar-nav']//a[contains(text(), 'MP3 Players')]");
    private final By showAllMP3 = By.xpath("//a[contains(@class, 'see-all') and contains(text(), 'Show AllMP3 Players')]");

    // FIXED: More robust navigation using hover and click
    public SearchPage goToDesktops() {
        hoverAndClick(desktopsMenu, showAllDesktops);
        waitForPageLoad();
        return this;
    }

    public SearchPage goToLaptops() {
        hoverAndClick(laptopsMenu, showAllLaptops);
        waitForPageLoad();
        return this;
    }

    public SearchPage goToMP3Players() {
        hoverAndClick(mp3PlayersMenu, showAllMP3);
        waitForPageLoad();
        return this;
    }

    public SearchPage goToTablets() {
        click(tabletsMenu);
        waitForPageLoad();
        return this;
    }

    public SearchPage goToPhonesAndPDAs() {
        click(phonesMenu);
        waitForPageLoad();
        return this;
    }

    // Helper method to wait for page load after navigation
    private void waitForPageLoad() {
        try {
            Thread.sleep(1000); // Small delay for page transition
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("content")));
    }

    // Products methods
    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> products = driver.findElements(productNames);
        for (WebElement product : products) {
            names.add(product.getText());
        }
        return names;
    }

    public List<String> getAllProductPrices() {
        List<String> prices = new ArrayList<>();
        for (WebElement price : driver.findElements(productPrices)) {
            prices.add(price.getText());
        }
        return prices;
    }

    public boolean hasResults() {
        return !driver.findElements(productItems).isEmpty();
    }

    public String getNoResultsMessage() {
        return areElementsPresent(noResultsMessage) ? getText(noResultsMessage) : "";
    }

    // Sorting
    public SearchPage sortBy(String option) {
        new Select(waitForElement(sortDropdown)).selectByVisibleText(option);
        waitForPageLoad();
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
            if (items.get(i).compareToIgnoreCase(items.get(i + 1)) > 0) return false;
        }
        return true;
    }

    public boolean isSortedDescending(List<String> items) {
        for (int i = 0; i < items.size() - 1; i++) {
            if (items.get(i).compareToIgnoreCase(items.get(i + 1)) < 0) return false;
        }
        return true;
    }

    // Breadcrumb
    public String getActiveBreadcrumb() {
        return getText(activeBreadcrumb);
    }

    public boolean isBreadcrumbCorrect(String expected) {
        return getActiveBreadcrumb().equalsIgnoreCase(expected);
    }

    // Left menu
    public String getActiveLeftMenuItem() {
        return areElementsPresent(activeLeftMenuItem) ? getText(activeLeftMenuItem) : "";
    }

    public boolean isLeftMenuItemHighlighted(String expected) {
        return getActiveLeftMenuItem().equalsIgnoreCase(expected);
    }

    public int getProductCount() {
        return getElementCount(productItems);
    }

    // Cart - FIXED with more robust selector
    public SearchPage addFirstProductToCart() {
        // More robust selector for add to cart button
        By addBtn = By.cssSelector(".product-layout .button-group button:first-child");
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addBtn));
        addButton.click();
        return this;
    }

    public String getAddToCartSuccessMessage() {
        By successAlert = By.cssSelector(".alert-success");
        if (areElementsPresent(successAlert)) {
            return getText(successAlert);
        }
        return "";
    }

    // Product navigation
    public ProductPage clickProduct(String productName) {
        for (WebElement product : waitForElements(productNames)) {
            if (product.getText().equalsIgnoreCase(productName)) {
                product.click();
                return new ProductPage(driver);
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    // NEW: Get any product name (first one)
    public String getFirstProductName() {
        List<String> products = getAllProductNames();
        if (!products.isEmpty()) {
            return products.get(0);
        }
        return null;
    }

    // Add this method to SearchPage class
    public boolean isProductDisplayed(String productName) {
        try {
            By productLink = By.linkText(productName);
            return areElementsPresent(productLink);
        } catch (Exception e) {
            return false;
        }
    }
}