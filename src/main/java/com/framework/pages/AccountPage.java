package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountPage extends BasePage {

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    private final By accountHeader = By.cssSelector("#content h2");
    private final By successMessage = By.xpath("//h1[contains(text(), 'Your Account Has Been Created!')]");
    private final By logoutLink = By.linkText("Logout");
    private final By myAccountText = By.xpath("//h2[contains(text(), 'My Account')]");

    public boolean isAccountPageDisplayed() {
        // Check for My Account header on account page
        return isDisplayed(myAccountText) || isDisplayed(accountHeader);
    }

    public boolean isRegistrationSuccessDisplayed() {
        return isDisplayed(successMessage);
    }

    public boolean isLogoutDisplayed() {
        return isDisplayed(logoutLink);
    }

    public HomePage logout() {
        click(logoutLink);
        return new HomePage(driver);
    }
}