package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import java.time.Duration;

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

    private final By successMessage = By.xpath("//h1[contains(text(), 'Your Account Has Been Created!')]");

    // Fixed error locators with contains for flexibility
    private final By firstNameError = By.xpath("//input[@id='input-firstname']/following-sibling::div[contains(@class, 'text-danger')]");
    private final By lastNameError = By.xpath("//input[@id='input-lastname']/following-sibling::div[contains(@class, 'text-danger')]");
    private final By emailError = By.xpath("//input[@id='input-email']/following-sibling::div[contains(@class, 'text-danger')]");
    private final By telephoneError = By.xpath("//input[@id='input-telephone']/following-sibling::div[contains(@class, 'text-danger')]");
    private final By passwordError = By.xpath("//input[@id='input-password']/following-sibling::div[contains(@class, 'text-danger')]");

    public AccountPage registerSuccess(String fName, String lName, String mail,
                                       String phone, String pass) {
        if (fName != null && !fName.equals("null") && !fName.isEmpty()) type(firstName, fName);
        if (lName != null && !lName.equals("null") && !lName.isEmpty()) type(lastName, lName);
        if (mail != null && !mail.equals("null") && !mail.isEmpty()) type(email, mail);
        if (phone != null && !phone.equals("null") && !phone.isEmpty()) type(telephone, phone);
        if (pass != null && !pass.equals("null") && !pass.isEmpty()) {
            type(password, pass);
            type(confirmPassword, pass);
        }
        click(agreeCheckbox);
        click(continueButton);
        return new AccountPage(driver);
    }

    public RegisterPage registerWithErrors(String fName, String lName, String mail,
                                           String phone, String pass) {
        if (fName != null && !fName.equals("null") && !fName.isEmpty()) type(firstName, fName);
        if (lName != null && !lName.equals("null") && !lName.isEmpty()) type(lastName, lName);
        if (mail != null && !mail.equals("null") && !mail.isEmpty()) type(email, mail);
        if (phone != null && !phone.equals("null") && !phone.isEmpty()) type(telephone, phone);
        if (pass != null && !pass.equals("null") && !pass.isEmpty()) {
            type(password, pass);
            type(confirmPassword, pass);
        }
        click(continueButton);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    // Fast error checking with reduced timeout
    private boolean isElementPresent(By locator) {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            boolean result = driver.findElement(locator).isDisplayed();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return result;
        } catch (NoSuchElementException e) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return false;
        }
    }

    public boolean isFirstNameErrorDisplayed() { return isElementPresent(firstNameError); }
    public boolean isLastNameErrorDisplayed() { return isElementPresent(lastNameError); }
    public boolean isEmailErrorDisplayed() { return isElementPresent(emailError); }
    public boolean isTelephoneErrorDisplayed() { return isElementPresent(telephoneError); }
    public boolean isPasswordErrorDisplayed() { return isElementPresent(passwordError); }

    public boolean isAnyErrorDisplayed() {
        return isFirstNameErrorDisplayed() || isLastNameErrorDisplayed() ||
                isEmailErrorDisplayed() || isTelephoneErrorDisplayed() ||
                isPasswordErrorDisplayed();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return driver.findElement(successMessage).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}