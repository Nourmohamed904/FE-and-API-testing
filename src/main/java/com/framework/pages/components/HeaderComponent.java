package com.framework.pages.components;

import com.framework.pages.CartPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.RegisterPage;
import com.framework.pages.SearchPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HeaderComponent {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public HeaderComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void type(By locator, String text) {
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForElement(locator).getText();
    }

    protected boolean areElementsPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    private final By myAccountMenu = By.cssSelector("a[title='My Account']");
    private final By loginOption = By.linkText("Login");
    private final By registerOption = By.linkText("Register");
    private final By logoutOption = By.xpath("//a[text()='Logout']");
    private final By searchBox = By.name("search");
    private final By searchButton = By.cssSelector(".input-group-btn button");
    private final By cartDropdownTrigger = By.cssSelector("#cart .btn-inverse");
    private final By viewCartLink = By.linkText("View Cart");
    private final By cartTotal = By.id("cart-total");
    private final By currencySelector = By.cssSelector("#form-currency button.dropdown-toggle");
    private final By euroCurrency = By.name("EUR");

    public HeaderComponent changeCurrencyToEuro() {
        click(currencySelector);
        click(euroCurrency);
        return this;
    }

    public CartPage goToShoppingCart() {
        click(cartDropdownTrigger);
        click(viewCartLink);
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

    public HomePage logout() {
        openMyAccountMenu();
        if (areElementsPresent(logoutOption)) {
            click(logoutOption);
        }
        return new HomePage(driver);
    }

    public SearchPage search(String productName) {
        type(searchBox, productName);
        click(searchButton);
        return new SearchPage(driver);
    }
}
