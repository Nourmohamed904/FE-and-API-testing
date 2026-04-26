package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountPage extends BasePage {

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    private final By accountHeader = By.cssSelector("#content h2");
    private final By logoutLink = By.linkText("Logout");

    public boolean isAccountPageDisplayed() {
        return isDisplayed(accountHeader);
    }

    public boolean isLogoutDisplayed() {
        header().openMyAccountMenu();
        boolean displayed = isDisplayed(logoutLink);
        header().openMyAccountMenu(); // Close menu
        return displayed;
    }

    public HomePage logout() {
        header().openMyAccountMenu();
        click(logoutLink);
        return new HomePage(driver);
    }
}