package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    // Navigation menus
    private final By desktopsMenu = By.linkText("Desktops");
    private final By showAllDesktops = By.linkText("Show All Desktops");

    private final By laptopsMenu = By.linkText("Laptops & Notebooks");
    private final By showAllLaptops = By.linkText("Show All Laptops & Notebooks");

    private final By tabletsMenu = By.linkText("Tablets");
    private final By phonesMenu = By.linkText("Phones & PDAs");

    private final By mp3PlayersMenu = By.linkText("MP3 Players");
    private final By showAllMP3 = By.linkText("Show All MP3 Players");

    // ✅ Navigation (FIXED)

    public SearchPage goToDesktops() {
        hoverAndClick(desktopsMenu, showAllDesktops);
        return this;
    }

    public SearchPage goToLaptops() {
        hoverAndClick(laptopsMenu, showAllLaptops);
        return this;
    }

    public SearchPage goToMP3Players() {
        hoverAndClick(mp3PlayersMenu, showAllMP3);
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

    // ✅ Products

    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement product : driver.findElements(productNames)) {
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
        return areElementsPresent(productItems);
    }

    public String getNoResultsMessage() {
        return areElementsPresent(noResultsMessage) ? getText(noResultsMessage) : "";
    }

    // ✅ Sorting

    public SearchPage sortBy(String option) {
        new Select(waitForElement(sortDropdown)).selectByVisibleText(option);
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

    // ✅ Breadcrumb

    public String getActiveBreadcrumb() {
        return getText(activeBreadcrumb);
    }

    public boolean isBreadcrumbCorrect(String expected) {
        return getActiveBreadcrumb().equalsIgnoreCase(expected);
    }

    // ✅ Left menu

    public String getActiveLeftMenuItem() {
        return areElementsPresent(activeLeftMenuItem) ? getText(activeLeftMenuItem) : "";
    }

    public boolean isLeftMenuItemHighlighted(String expected) {
        return getActiveLeftMenuItem().equalsIgnoreCase(expected);
    }

    public int getProductCount() {
        return getElementCount(productItems);
    }

    // ✅ Cart

    public SearchPage addFirstProductToCart() {
        By addBtn = By.cssSelector(".product-layout:first-child button[onclick*='cart.add']");
        click(addBtn);
        return this;
    }

    public String getAddToCartSuccessMessage() {
        By successAlert = By.cssSelector(".alert-success");
        return areElementsPresent(successAlert) ? getText(successAlert) : "";
    }

    // ✅ Product navigation

    public ProductPage clickProduct(String productName) {
        for (WebElement product : waitForElements(productNames)) {
            if (product.getText().equalsIgnoreCase(productName)) {
                product.click();
                return new ProductPage(driver);
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }
}