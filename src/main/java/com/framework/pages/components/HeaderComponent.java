package com.framework.pages.components;

import com.framework.base.BasePage;
import com.framework.pages.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderComponent extends BasePage {

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    private final By myAccountMenu = By.cssSelector("a[title='My Account']");
    private final By loginOption = By.linkText("Login");
    private final By registerOption = By.linkText("Register");
    private final By logoutOption = By.linkText("Logout");
    private final By searchBox = By.name("search");
    private final By searchButton = By.cssSelector(".input-group-btn button");
    private final By cartDropdownTrigger = By.cssSelector("#cart .btn-inverse");
    private final By viewCartLink = By.linkText("View Cart");
    private final By cartTotal = By.id("cart-total");
    private final By currencySelector = By.id("form-currency");
    private final By euroCurrency = By.name("EUR");
    private final By poundCurrency = By.name("GBP");
    private final By dollarCurrency = By.name("USD");

    // Currency methods (for "Change currency" test)
    public HeaderComponent changeCurrencyToEuro() {
        click(currencySelector);
        click(euroCurrency);
        return this;
    }

    public HeaderComponent changeCurrencyToPound() {
        click(currencySelector);
        click(poundCurrency);
        return this;
    }

    public HeaderComponent changeCurrencyToDollar() {
        click(currencySelector);
        click(dollarCurrency);
        return this;
    }

    // Shopping cart methods
    public CartPage goToShoppingCart() {
        click(cartDropdownTrigger);
        click(viewCartLink);
        return new CartPage(driver);
    }

    public String getCartTotalText() {
        return getText(cartTotal);
    }

    // My Account menu methods
    public HeaderComponent openMyAccountMenu() {
        click(myAccountMenu);
        return this;
    }

    public LoginPage goToLogin() {
        openMyAccountMenu();
        click(loginOption);
        return new LoginPage(driver);
    }

    public RegisterPage goToRegister() {
        openMyAccountMenu();
        click(registerOption);
        return new RegisterPage(driver);
    }

    public HomePage logout() {
        openMyAccountMenu();
        click(logoutOption);
        return new HomePage(driver);
    }

    public boolean isUserLoggedIn() {
        openMyAccountMenu();
        boolean hasLogout = areElementsPresent(logoutOption);
        click(myAccountMenu);
        return hasLogout;
    }

    // Search method - returns ProductListPage
    public SearchPage search(String productName) {
        type(searchBox, productName);
        click(searchButton);
        return new SearchPage(driver);
    }
}