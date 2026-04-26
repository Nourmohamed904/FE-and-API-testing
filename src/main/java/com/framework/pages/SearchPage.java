package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    private final By tabletsMenu = By.xpath("//ul[@class='nav navbar-nav']/li/a[contains(text(),'Tablets')]");
    private final By phonesMenu = By.xpath("//ul[@class='nav navbar-nav']/li/a[contains(text(),'Phones & PDAs')]");

    // Category navigation methods
    public SearchPage goToDesktops() {
        click(desktopsMenu);
        // Wait a bit for submenu to appear
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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

    public String getNoResultsMessage() {
        try {
            return driver.findElement(noResultsMessage).getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }
}