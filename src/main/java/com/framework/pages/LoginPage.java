package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    private final By emailField = By.id("input-email");
    private final By passwordField = By.id("input-password");
    private final By loginButton = By.cssSelector("input[value='Login']");
    private final By errorMessage = By.cssSelector(".alert-danger");

    public AccountPage loginValid(String email, String password) {
        type(emailField, email);
        type(passwordField, password);
        click(loginButton);
        return new AccountPage(driver);
    }

    public LoginPage loginInvalid(String email, String password) {
        type(emailField, email);
        type(passwordField, password);
        click(loginButton);
        return this;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}