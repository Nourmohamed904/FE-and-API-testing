package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPage extends BasePage {

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    private final By productTitles = By.cssSelector(".product-thumb h4 a");
    private final By noResultsMessage = By.cssSelector("#content p");

    public boolean hasResults() {
        return !driver.findElements(productTitles).isEmpty();
    }

    public String getNoResultsMessage() {
        return getText(noResultsMessage);
    }
}