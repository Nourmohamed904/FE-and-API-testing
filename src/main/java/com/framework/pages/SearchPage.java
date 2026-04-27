package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import java.util.ArrayList;
import java.util.List;

public class SearchPage extends BasePage {

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    // Product listing elements
    private final By productItems = By.cssSelector(".product-layout");
    private final By productNames = By.cssSelector(".product-layout .caption h4 a");
    private final By productPrices = By.cssSelector(".product-layout .price");
    private final By noResultsMessage = By.cssSelector("#content p");

    // Sorting elements
    private final By sortDropdown = By.id("input-sort");

    // Breadcrumb elements
    private final By activeBreadcrumb = By.cssSelector(".breadcrumb li:last-child");

    // Left side menu
    private final By leftSideMenu = By.cssSelector(".list-group a");
    private final By activeLeftMenuItem = By.cssSelector(".list-group a.active");

    // Navigation methods - Using correct selectors for the site
    private final By desktopsMenu = By.xpath("//ul[@class='nav navbar-nav']/li/a[contains(text(),'Desktops')]");
    private final By showAllDesktops = By.xpath("//a[contains(text(),'Show All Desktops')]");
    private final By laptopsMenu = By.xpath("//ul[@class='nav navbar-nav']/li/a[contains(text(),'Laptops')]");
    private final By showAllLaptops = By.xpath("//a[contains(text(),'Show All Laptops')]");
    private final By tabletsMenu = By.xpath("//ul[@class='nav navbar-nav']/li/a[contains(text(),'Tablets')]");
    private final By phonesMenu = By.xpath("//ul[@class='nav navbar-nav']/li/a[contains(text(),'Phones & PDAs')]");
    private final By mp3PlayersMenu = By.xpath("//ul[@class='nav navbar-nav']/li/a[contains(text(),'MP3 Players')]");

    // NEW: Advanced search locators
    private final By advancedSearchKeyword = By.id("input-search");
    private final By categoryDropdown = By.name("category_id");
    private final By subcategoryCheckbox = By.name("sub_category");
    private final By advancedSearchButton = By.id("button-search");
    private final By searchDescriptionCheckbox = By.name("description");
    private final By productLinks = By.cssSelector(".product-layout .caption h4 a");

    // Category navigation methods with hover
    public SearchPage goToDesktops() {
        WebElement desktopsElement = waitForElement(desktopsMenu);
        Actions actions = new Actions(driver);
        actions.moveToElement(desktopsElement).perform();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        click(showAllDesktops);
        return this;
    }

    public SearchPage goToLaptops() {
        WebElement laptopsElement = waitForElement(laptopsMenu);
        Actions actions = new Actions(driver);
        actions.moveToElement(laptopsElement).perform();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        click(showAllLaptops);
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
        WebElement mp3Element = waitForElement(mp3PlayersMenu);
        Actions actions = new Actions(driver);
        actions.moveToElement(mp3Element).perform();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        click(By.xpath("//a[contains(text(),'Show All MP3 Players')]"));
        return this;
    }

    // Search result methods
    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        if (areElementsPresent(productNames)) {
            List<WebElement> productElements = driver.findElements(productNames);
            for (WebElement product : productElements) {
                names.add(product.getText());
            }
        }
        return names;
    }

    public List<String> getAllProductPrices() {
        List<String> prices = new ArrayList<>();
        if (areElementsPresent(productPrices)) {
            List<WebElement> priceElements = driver.findElements(productPrices);
            for (WebElement price : priceElements) {
                prices.add(price.getText());
            }
        }
        return prices;
    }

    public boolean hasResults() {
        return areElementsPresent(productItems);
    }

    public String getNoResultsMessage() {
        if (areElementsPresent(noResultsMessage)) {
            return getText(noResultsMessage);
        }
        return "";
    }

    // Sorting methods
    public SearchPage sortBy(String sortOption) {
        Select sortSelect = new Select(waitForElement(sortDropdown));
        sortSelect.selectByVisibleText(sortOption);
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

    // Breadcrumb methods
    public String getActiveBreadcrumb() {
        return getText(activeBreadcrumb);
    }

    public boolean isBreadcrumbCorrect(String expectedLastCrumb) {
        String actual = getActiveBreadcrumb();
        return actual.equalsIgnoreCase(expectedLastCrumb);
    }

    // Left side menu methods
    public String getActiveLeftMenuItem() {
        if (areElementsPresent(activeLeftMenuItem)) {
            return getText(activeLeftMenuItem);
        }
        return "";
    }

    public boolean isLeftMenuItemHighlighted(String expectedItem) {
        String actual = getActiveLeftMenuItem();
        return actual.equalsIgnoreCase(expectedItem);
    }

    public int getProductCount() {
        return getElementCount(productItems);
    }

    // ========== NEW ADVANCED SEARCH METHODS ==========

    // Go to advanced search page
    public SearchPage goToAdvancedSearch() {
        driver.get(driver.getCurrentUrl().replace("/index.php?route=common/home",
                "/index.php?route=product/search"));
        return this;
    }

    // Set search keyword
    public SearchPage setSearchKeyword(String keyword) {
        if (keyword != null && !keyword.equals("null") && !keyword.isEmpty()) {
            type(advancedSearchKeyword, keyword);
        }
        return this;
    }

    // Select category from dropdown
    public SearchPage selectCategory(String categoryName) {
        if (categoryName != null && !categoryName.equals("null") && !categoryName.isEmpty()) {
            Select select = new Select(waitForElement(categoryDropdown));
            select.selectByVisibleText(categoryName);
        }
        return this;
    }

    // Enable/disable search in subcategories
    public SearchPage setSearchInSubcategories(boolean enabled) {
        try {
            WebElement checkbox = waitForElement(subcategoryCheckbox);
            if (enabled && !checkbox.isSelected()) {
                checkbox.click();
            } else if (!enabled && checkbox.isSelected()) {
                checkbox.click();
            }
        } catch (Exception e) {
            // Checkbox might not exist or be disabled, log but continue
            System.out.println("Could not interact with subcategory checkbox: " + e.getMessage());
        }
        return this;
    }

    // Enable/disable search in product descriptions
    public SearchPage setSearchInDescriptions(boolean enabled) {
        try {
            WebElement checkbox = driver.findElement(searchDescriptionCheckbox);
            if (enabled && !checkbox.isSelected()) {
                checkbox.click();
            } else if (!enabled && checkbox.isSelected()) {
                checkbox.click();
            }
        } catch (Exception e) {
            // Checkbox might not exist, log but continue
            System.out.println("Could not interact with description checkbox: " + e.getMessage());
        }
        return this;
    }

    // Perform advanced search
    public SearchPage performAdvancedSearch() {
        click(advancedSearchButton);
        return this;
    }

    // Check if no results message is displayed
    public boolean isNoResultsMessageDisplayed() {
        try {
            String message = getNoResultsMessage();
            return message.toLowerCase().contains("no product") ||
                    message.toLowerCase().contains("there is no product");
        } catch (Exception e) {
            return false;
        }
    }

    // Get no results message text
    public String getNoResultsMessageText() {
        try {
            return getNoResultsMessage();
        } catch (Exception e) {
            return "";
        }
    }

    // Check if specific product exists in results
    public boolean isProductDisplayed(String productName) {
        if (productName == null || productName.isEmpty()) {
            return false;
        }
        List<WebElement> products = driver.findElements(productLinks);
        for (WebElement product : products) {
            if (product.getText().contains(productName)) {
                return true;
            }
        }
        return false;
    }

    // Get all product names from search results
    public List<String> getSearchResultProductNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> products = driver.findElements(productLinks);
        for (WebElement product : products) {
            names.add(product.getText());
        }
        return names;
    }
}