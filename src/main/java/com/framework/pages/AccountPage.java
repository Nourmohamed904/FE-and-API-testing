package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountPage extends BasePage {

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    private final By accountHeader = By.cssSelector("#content h2");

    public boolean isAccountPageDisplayed() {
        return isDisplayed(accountHeader);
    }
}