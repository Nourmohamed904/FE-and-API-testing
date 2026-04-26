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

    // Success message after registration
    private final By successMessage = By.xpath("//h1[contains(text(), 'Your Account Has Been Created!')]");

    // Error messages for validation
    private final By firstNameError = By.xpath("//div[contains(@class, 'text-danger')]//following-sibling::div[contains(text(), 'First Name')]");
    private final By lastNameError = By.xpath("//div[contains(@class, 'text-danger')]//following-sibling::div[contains(text(), 'Last Name')]");
    private final By emailError = By.xpath("//div[contains(@class, 'text-danger')]//following-sibling::div[contains(text(), 'E-Mail')]");
    private final By telephoneError = By.xpath("//div[contains(@class, 'text-danger')]//following-sibling::div[contains(text(), 'Telephone')]");
    private final By passwordError = By.xpath("//div[contains(@class, 'text-danger')]//following-sibling::div[contains(text(), 'Password')]");

    // Successful registration
    public AccountPage registerSuccess(String fName, String lName, String mail,
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

    // Registration with errors - doesn't click agree (for testing error scenarios)
    public RegisterPage registerWithErrors(String fName, String lName, String mail,
                                           String phone, String pass) {
        // Only type non-null and non-empty values
        if (fName != null && !fName.isEmpty()) type(firstName, fName);
        if (lName != null && !lName.isEmpty()) type(lastName, lName);
        if (mail != null && !mail.isEmpty()) type(email, mail);
        if (phone != null && !phone.isEmpty()) type(telephone, phone);
        if (pass != null && !pass.isEmpty()) {
            type(password, pass);
            type(confirmPassword, pass);
        }
        // Don't click agree checkbox - this should cause validation errors
        click(continueButton);
        return this;
    }

    // Error validation methods
    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successMessage);
    }

    public boolean isFirstNameErrorDisplayed() {
        return isDisplayed(firstNameError);
    }

    public boolean isLastNameErrorDisplayed() {
        return isDisplayed(lastNameError);
    }

    public boolean isEmailErrorDisplayed() {
        return isDisplayed(emailError);
    }

    public boolean isTelephoneErrorDisplayed() {
        return isDisplayed(telephoneError);
    }

    public boolean isPasswordErrorDisplayed() {
        return isDisplayed(passwordError);
    }

    public String getPasswordErrorMessage() {
        return getText(passwordError);
    }
}