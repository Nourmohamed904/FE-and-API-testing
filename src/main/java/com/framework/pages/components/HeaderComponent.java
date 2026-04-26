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
    private final By searchBox = By.name("search");
    private final By searchButton = By.cssSelector("button.btn.btn-default.btn-lg");
    private final By shoppingCartIcon = By.cssSelector("a[title='Shopping Cart']");
    private final By cartTotal = By.id("cart-total");

    public CartPage goToShoppingCart() {
        click(shoppingCartIcon);
        return new CartPage(driver);
    }

    public String getCartTotalText() {
        return getText(cartTotal);
    }

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

    public SearchPage search(String productName) {
        type(searchBox, productName);
        click(searchButton);
        return new SearchPage(driver);
    }
}