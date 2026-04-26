package tests;

import base.BaseTest;
import com.framework.pages.*;
import com.framework.utils.ExcelReader;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Authentication")
@Feature("Registration Functionality")
public class RegisterTest extends BaseTest {

    @DataProvider(name = "registerData")
    public Object[][] getRegisterData() {
        ExcelReader reader = new ExcelReader("testdata/testdata.xlsx", "Register");
        return reader.getData();
    }

    @Test(dataProvider = "registerData")
    @Description("Test user registration with valid and invalid data")
    @Story("User Registration")
    public void testRegister(String firstName, String lastName, String email,
                             String telephone, String password,
                             String expectedResult, String expectedErrorField) {

        Allure.addAttachment("Test Data", "First Name: " + firstName +
                ", Last Name: " + lastName +
                ", Email: " + email +
                ", Expected: " + expectedResult);

        HomePage home = new HomePage(driver);
        RegisterPage register = home.header().goToRegister();

        if (expectedResult.equalsIgnoreCase("success")) {
            Allure.addAttachment("Action", "Attempting successful registration");
            AccountPage account = register.registerSuccess(firstName, lastName, email, telephone, password);

            // Check for success message or account page
            boolean success = account.isRegistrationSuccessDisplayed() || account.isAccountPageDisplayed();
            Assert.assertTrue(success, "Registration should be successful");
            Allure.addAttachment("Result", "Registration successful");
        } else {
            Allure.addAttachment("Action", "Attempting registration with errors - Expected error field: " + expectedErrorField);
            register.registerWithErrors(firstName, lastName, email, telephone, password);

            // Verify specific error based on expected_error_field
            boolean errorDisplayed = false;
            if (expectedErrorField != null && !expectedErrorField.isEmpty() && !expectedErrorField.equals("null")) {
                switch (expectedErrorField.toLowerCase()) {
                    case "firstname":
                        errorDisplayed = register.isFirstNameErrorDisplayed();
                        break;
                    case "lastname":
                        errorDisplayed = register.isLastNameErrorDisplayed();
                        break;
                    case "email":
                        errorDisplayed = register.isEmailErrorDisplayed();
                        break;
                    case "telephone":
                        errorDisplayed = register.isTelephoneErrorDisplayed();
                        break;
                    case "password":
                        errorDisplayed = register.isPasswordErrorDisplayed();
                        break;
                }
            } else {
                // If no specific field, check if any error appears
                errorDisplayed = register.isFirstNameErrorDisplayed() ||
                        register.isLastNameErrorDisplayed() ||
                        register.isEmailErrorDisplayed() ||
                        register.isTelephoneErrorDisplayed() ||
                        register.isPasswordErrorDisplayed();
            }

            Assert.assertTrue(errorDisplayed,
                    "Expected error should be displayed for: " + expectedErrorField);
            Allure.addAttachment("Result", "Expected error displayed correctly");
        }
    }
}