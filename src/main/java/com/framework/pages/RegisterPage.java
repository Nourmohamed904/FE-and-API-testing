package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    private final By firstName = By.id("input-firstname");
    private final By lastName = By.id("input-lastname");
    private final By email = By.id("input-email");
    private final By telephone = By.id("input-telephone");
    private final By password = By.id("input-password");
    private final By confirmPassword = By.id("input-confirm");
    private final By agreeCheckbox = By.name("agree");
    private final By continueButton = By.cssSelector("input[value='Continue']");

    public AccountPage register(String fName, String lName, String mail,
                                String phone, String pass) {

        type(firstName, fName);
        type(lastName, lName);
        type(email, mail);
        type(telephone, phone);
        type(password, pass);
        type(confirmPassword, pass);
        click(agreeCheckbox);
        click(continueButton);

        return new AccountPage(driver);
    }
}